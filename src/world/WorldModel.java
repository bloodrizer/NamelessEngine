/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import events.EEntityChangeChunk;
import events.EEntitySpawn;
import events.ETakeDamage;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EEntityMove;
import game.combat.Damage.DamageType;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.decals.EntDecalBlood;
import java.util.Iterator;
import java.util.Map;
import ne.Game;
import org.lwjgl.util.Point;
import ui.GameUI;
import world.generators.ChunkGenerator;
import world.generators.ChunkGroundGenerator;
import world.layers.WorldLayer;
import world.util.LazyLoadWorldElement;
import world.util.astar.Mover;
import world.util.astar.TileBasedMap;

/**
 *
 * @author Administrator
 */

/*
 * This class handles world terrain data, including landscape, moisture, lightning model, object distribution, minerals, etc.
 */
public class WorldModel implements IEventListener {

    public static final int LAYER_COUNT = 10;    //max depth of geometry layers
    public static final int GROUND_LAYER = 0;

    
    //--------------------------------------------------------------------------
    private static Point util_point     = new Point(0,0);
    private static Point __stack_point  = new Point(0,0);

    private static boolean light_outdated = false;  //shows if model should rebuild terrain lightning

    //--------------------------------------------------------------------------
    private static LazyLoadWorldElement<WorldRegion> world_regions = 
    new LazyLoadWorldElement<WorldRegion>(){

        @Override
        public WorldRegion precache(Point origin) {
            //throw new UnsupportedOperationException("Not supported yet.");
            //Point region_coord = WorldRegion.get_region_coord(origin);
            
            WorldRegion region = new WorldRegion();
            region.origin.setLocation(origin);
            
            //TODO: load region from some external storage
            //(i.e. use server API)
            region.load_data();
            
            return region;
        }
    };


    private static java.util.HashMap<Integer, WorldLayer> world_layers 
            = new java.util.HashMap<Integer, WorldLayer>(LAYER_COUNT);
    
    static {
        //initialize layers
        for (int i = 0; i< LAYER_COUNT; i++ ){
            WorldLayer layer = new WorldLayer();
            layer.set_zindex(i);
            world_layers.put(i, layer);
        }
    }
    
    public WorldModel(){
        EventManager.subscribe((IEventListener) this);
        
    }
    
    public static WorldLayer get_layer(int layer_id){
        WorldLayer layer = world_layers.get(layer_id);
        return layer;
    }

    protected static java.util.Map<Point,WorldTile> get_tile_data(int layer_id){
        WorldLayer layer = world_layers.get(layer_id);
        if (layer!=null){
            return layer.get_tile_data();
        }
        
        throw new RuntimeException("Failed to access tile data at layer #"+layer_id);
    }
    
    public static void set_tile(Point origin, WorldTile tile, int z_index){
        get_tile_data(z_index).put(origin, tile);
    }

    public static void invalidate_light(){
        light_outdated = true;
    }

    //todo: use actual stack there
    private static void push_point(Point point){
        __stack_point.setLocation(point);
    }
    private static void pop_point(Point point){
        point.setLocation(__stack_point);
    }

    public static synchronized WorldTile get_tile(int x, int y, int z_index){
        push_point(util_point);
        util_point.setLocation(x, y);
        
        WorldTile tile = get_tile_data(z_index).get(util_point);
        pop_point(util_point);

        return tile;
    }
    
    public static synchronized WorldTile get_tile(int x, int y){
        return get_tile(x,y, GROUND_LAYER);
    }

    //DEPRECATED DEPRECATED DEPRECATED
    public static boolean tile_blocked(Point tile_origin){
        return false;
    }

    private static synchronized WorldChunk get_chunk(Point location){
        return get_chunk(location.getX(),location.getY());
    }
    
    private static synchronized WorldChunk get_chunk(int x, int y, int z_index){
        push_point(util_point);
        util_point.setLocation(x, y);

        WorldChunk chunk = get_layer(z_index).get_chunk(util_point);
        //WorldChunk chunk = chunk_data.get(new Point(x,y));
        pop_point(util_point);

        return chunk;
    }
    
    private static synchronized WorldChunk get_chunk(int x, int y){
       return get_chunk(x, y, GROUND_LAYER);
    }

    public static synchronized WorldChunk get_cached_chunk(int chunk_x, int chunk_y, int z_index){
        WorldChunk chunk = get_chunk(chunk_x, chunk_y);
        if (chunk == null){
            chunk = precache_chunk(chunk_x, chunk_y);
        }
        return chunk;
    }
    
    public static synchronized WorldChunk get_cached_chunk(int chunk_x, int chunk_y){
        return get_cached_chunk(chunk_x, chunk_y, GROUND_LAYER);
    }
    
    public static synchronized WorldChunk get_cached_chunk(Point location){
        return get_cached_chunk(location.getX(),location.getY(), GROUND_LAYER);
    }

    public void update(){
        //1. update timer data
        //2. check if think call is allowed
        //3. call think
        WorldTimer.tick();


        Object[] ent_list = EntityManager.ent_list_sync.toArray();
        for(int i=EntityManager.ent_list_sync.size()-1; i>=0; i--){
            Entity entity = (Entity)ent_list[i];

            entity.update();

            if (entity.is_awake(Timer.get_time())){
                  entity.think();
            }
            if (entity.is_next_frame(Timer.get_time())){
                  entity.next_frame();
            }

            if (entity.is_garbage()){
                EntityManager.remove_entity(entity);
                entity.tile.remove_entity(entity);
            }
        }

        //here comes tricky part - recalculate light emission
        if (light_outdated){
            recalculate_light();
            light_outdated = false;
        }
        
    }

    public void recalculate_light(){

        Object[] ent_list = EntityManager.ent_list_sync.toArray();

        int x = WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;

        for (int i = x; i<x+size; i++)
        for (int j = y; j<y+size; j++){
             int chunk_x = (int)Math.floor((float)i / WorldChunk.CHUNK_SIZE);
             int chunk_y = (int)Math.floor((float)j / WorldChunk.CHUNK_SIZE);

             if (WorldModel.get_cached_chunk(
                        chunk_x,
                        chunk_y) != null)
             {
                 WorldTile tile = WorldModel.get_tile(i,j);

                 if (tile != null){
                     tile.light_level = 0.0f;

                     for(int e_id=0; e_id<ent_list.length; e_id++){
                         Entity entity = (Entity)ent_list[e_id];
                         if (entity.light_amt > 0.0f){
                            tile.light_level += get_light_amt(i,j,entity);
                         }
                         //System.out.println(tile.light_level);
                     }
                 }
             }
        }
    }

    /*
     * This function returns light ammount, casted by entity 'ent' at the tile
     */
    private float get_light_amt(int x, int y, Entity ent){
        int dx = ent.origin.getX()-x;
        int dy = ent.origin.getY()-y;
        //float disst = (float)Math.sqrt(dx*dx+dy*dy);
        float disst = dx*dx+dy*dy;

        return ent.light_amt / disst;

        //return 0.1f;
    }
    
    /*
     * Put new chunk with set (x,y) point index and fills it with terrain data
     * Use ground layer as default for compatibility reason
     * Note that we probably need to build every layer in this chunk(?)
     */

    public static WorldChunk precache_chunk(int x, int y){
        return precache_chunk(x, y, GROUND_LAYER);
    }
    
    private static WorldChunk precache_chunk(int x, int y, int z_index){
        WorldChunk chunk = new WorldChunk(x, y);
        WorldLayer layer = get_layer(z_index);

        

        layer.set_chunk(new Point(x,y), chunk);
        build_chunk(chunk.origin, z_index);

        return chunk;
    }



    public static void build_chunk(Point origin, int z_index){  
        //todo: check z_index there
        ChunkGenerator ground_gen = new ChunkGroundGenerator();
        ground_gen.set_zindex(z_index);
        
        ground_gen.generate(origin);
    }
    


    //clean all unused chunks and data
    public synchronized void chunk_gc(){
        WorldLayer layer = get_layer(GROUND_LAYER);
        layer.gc();
    }


    public static synchronized void move_entity(Entity entity, Point coord_dest){
        Point coord_from = new Point(entity.origin);  //defence copy
        
        //System.out.println("world model::on entity move to:"+dest.toString());
        entity.origin.setLocation(coord_dest);

        if (entity.light_amt > 0.0f){
            invalidate_light();
        }

        //now with a chunk shit
        //----------------------------------------------------------------------
        WorldChunk new_chunk = get_cached_chunk(WorldChunk.get_chunk_coord(coord_dest));
        if (new_chunk != null && !entity.in_chunk(new_chunk)){

            WorldChunk ent_chunk = entity.get_chunk();
            //todo: move to event dispatcher?
            if(ent_chunk != null ){
                ent_chunk.remove_entity(entity);
            }

            new_chunk.add_entity(entity);
            //todo end

            //------------------------------------------------------------------
            EEntityChangeChunk e_change_chunk = new EEntityChangeChunk(entity,ent_chunk,new_chunk);
            e_change_chunk.post();
            //----------------------------------------------------------------------
        }
        //now, after we succesfuly performed chunk routime,
        //set valid entity pointers in chunks
        WorldTile tile_from = get_tile(coord_from.getX(), coord_from.getY());
        WorldTile tile_to   = get_tile(coord_dest.getX(), coord_dest.getY());

        tile_from.remove_entity(entity);
        tile_to.add_entity(entity);

        //----------------------------------------------------------------------
    }



    //----------------------------EVENTS SHIT-----------------------------------
    public void e_on_event(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;
           move_entity(move_event.entity, move_event.getTo());

           if (move_event.entity.isPlayerEnt()){
               WorldViewCamera.target.setLocation(move_event.entity.origin);
           }
       }
       else if(event instanceof EEntitySpawn){
           EEntitySpawn spawn_event = (EEntitySpawn)event;
           //-------------------------------------------------------------------
           WorldChunk new_chunk = get_cached_chunk(WorldChunk.get_chunk_coord(spawn_event.ent.origin));
           
           EEntityChangeChunk e_change_chunk = new EEntityChangeChunk(spawn_event.ent,null,new_chunk);
           e_change_chunk.post();

           Point ent_origin = spawn_event.ent.origin;
           WorldTile spawn_tile = get_tile(ent_origin.getX(), ent_origin.getY());
           
           /* Some ents are prohabited from spawning. Normaly, that should be checked at the server side
            *
            * For now we will check it at client side (totems for example) 
            */
           Point region_coord = WorldRegion.get_region_coord(ent_origin);
           //TODO: get region instance and check if totem can be build there
           
           if (spawn_tile != null){
                spawn_tile.add_entity(spawn_event.ent);
           }else{
               System.err.println("Failed to assign spawned entity to tile: tile is null!");
           }

           if (spawn_event.ent.light_amt > 0.0f){
                invalidate_light();
           }


           //spawn_event.ent.origin = spawn_event.origin;
           //-------------------------------------------------------------------
       }else if(event instanceof EEntityChangeChunk){

           

           EEntityChangeChunk e_change_chunk = (EEntityChangeChunk)event;

           //System.err.println("setting new chunk @ for ent "+Entity.toString(e_change_chunk.ent));
           
           Entity ent = e_change_chunk.ent;
           //ent.set_chunk(e_change_chunk.to);
           e_change_chunk.to.add_entity(ent);

           if (ent.isPlayerEnt()){
                update_terrain();

                WorldCluster.locate(e_change_chunk.to.origin);
                chunk_gc();
           }

       }else if(event instanceof ETakeDamage){
           //drop blood if someone is taking damage
           ETakeDamage dmg_event = (ETakeDamage)event;
           if (dmg_event.dmg.type != DamageType.DMG_FIRE &&
               dmg_event.dmg.type != DamageType.DMG_MAGIC ){
               Point dmg_origin = new Point();
               dmg_origin.setLocation(dmg_event.ent.origin);

               WorldTile tile = get_tile(dmg_origin.getX(), dmg_origin.getY());
               if (!tile.has_ent(EntDecalBlood.class)){
                   EntDecalBlood blood = new EntDecalBlood();
                   blood.spawn(dmg_origin);

                   //TODO: set random dx, dy for more natural blood drops?
               }
           }
       }


    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;
           move_entity(move_event.entity, move_event.getFrom());
       }
    }

    //--------------------------------------------------------------------------
    static final int MAP_SIZE = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;
    public static WorldModelTileMap tile_map = new WorldModelTileMap();

    /*
     * update_terrain is called whenever player_ent crosses a border of terrain
     and new portion of terrain generation is required
     *
     */

    private void update_terrain() {
        Terrain.heightmap_cached.clear();
        //System.out.println("clearing aquatic tiles data");
        //Terrain.aquatic_tiles.clear();

        GameUI ui = (GameUI)(Game.get_game_mode().get_ui());
        
        /*if(ui.minimap != null){
            ui.minimap.update_map();
        }*/
    }

    /*
     *  WorldModelTileMap is a mediator between WorldModel and AStarPathfinder
     *
     *  it's a relatively small tilemap, that is using 0,0 - MAP_SIZE,MAP_SIZE local coord
     *  for fast path calculation
     *
     *
     *
     */

    public static class WorldModelTileMap implements TileBasedMap {

        public void pathFinderVisited(int x, int y) {
            //visited[x][y] = true;
        }


        public int getWidthInTiles() {
            return MAP_SIZE;
        }

        public int getHeightInTiles() {
            return MAP_SIZE;
        }

        Point temp = new Point(0,0);
        public boolean blocked(Mover mover, int x, int y) {
            //throw new UnsupportedOperationException("Not supported yet.");
            //todo: check the mover type
            temp.setLocation(x,y);
            temp = local2world(temp);

            WorldTile tile = get_tile(temp.getX(), temp.getY());
            if (tile == null){
                return true;
            }

            return tile.is_blocked();

            //todo: check border collision
        }

        public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
            return 1;
            //TODO: calculate different terrain types there
        }

        static Point origin = new Point(0,0);
        public synchronized Point world2local(Point world){
            origin.setLocation(
                    WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE,
                    WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE);

            world.setLocation(
                    world.getX()-origin.getX(),
                    world.getY()-origin.getY()
            );
            return world;
        }

        public synchronized Point local2world(Point world){
            origin.setLocation(
                    WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE,
                    WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE);

            world.setLocation(
                    world.getX()+origin.getX(),
                    world.getY()+origin.getY()
            );
            return world;
        }
    }


    //--------------------------------------------------------------------------
}
