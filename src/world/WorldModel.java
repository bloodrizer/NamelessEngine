/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import events.EEntityChangeChunk;
import events.EEntitySpawn;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EEntityMove;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.EntityPlayer;
import game.ent.enviroment.EntityStone;
import game.ent.enviroment.EntityTree;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import ne.Game;
import ne.Main;
import org.lwjgl.util.Point;
import player.Player;
import ui.GameUI;
import world.WorldTile.TerrainType;
import world.generators.ChestGenerator;
import world.generators.GrassGenerator;
import world.generators.TreeGenerator;
import world.util.NLTimer;
import world.util.astar.Mover;
import world.util.astar.TileBasedMap;

/**
 *
 * @author Administrator
 */
public class WorldModel implements IEventListener {
    //

    private static java.util.Map<Point,WorldTile> tile_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldTile>(1000));
    //--------------------------------------------------------------------------
    private static Point util_point     = new Point(0,0);
    private static Point __stack_point  = new Point(0,0);

    private static boolean light_outdated = false;  //shows if model should rebuild terrain lightning

    //--------------------------------------------------------------------------
    private static java.util.Map<Point,WorldChunk> chunk_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldChunk>(100));

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

    public static synchronized WorldTile get_tile(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);
        
        WorldTile tile = tile_data.get(util_point);
        pop_point(util_point);

        return tile;
    }

    //DEPRECATED DEPRECATED DEPRECATED
    public static boolean tile_blocked(Point tile_origin){
        return false;
    }

    private static synchronized WorldChunk get_chunk(Point location){
        return get_chunk(location.getX(),location.getY());
    }
    private static synchronized WorldChunk get_chunk(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);

        WorldChunk chunk = chunk_data.get(util_point);
        //WorldChunk chunk = chunk_data.get(new Point(x,y));
        pop_point(util_point);

        return chunk;
    }
    //TODO: move to the WorldChunk
    public static Point get_chunk_coord(Point position) {
        //todo: use util point?
        //int CL_OFFSET = (WorldCluster.CLUSTER_SIZE-1)/2;

        int cx = (int)Math.floor((float)position.getX() / WorldChunk.CHUNK_SIZE);
        int cy = (int)Math.floor((float)position.getY() / WorldChunk.CHUNK_SIZE);

        return new Point(cx,cy);
    }

    public static synchronized WorldChunk get_cached_chunk(int chunk_x, int chunk_y){
        WorldChunk chunk = get_chunk(chunk_x, chunk_y);
        if (chunk == null){
            chunk = precache_chunk(chunk_x, chunk_y);
        }
        return chunk;
    }
    public static synchronized WorldChunk get_cached_chunk(Point location){
        return get_cached_chunk(location.getX(),location.getY());
    }


    public WorldModel(){
        EventManager.subscribe((IEventListener) this);
    }

    public void update(){
        //1. update timer data
        //2. check if think call is allowed
        //3. call think
        Timer.tick();
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
        //TODO: use tile array iteration
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
                     //Main.game.running = false;
                     //System.exit(0);


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

    public static WorldChunk precache_chunk(int x, int y){
        //NOTE: safe switch, debug only
        /*if (!WorldCluster.chunk_in_cluster(new Point(x,y))){
            return null;
        }*/

        WorldChunk chunk = new WorldChunk(x, y);
        //build_chunk(chunk.origin);

        chunk_data.put(new Point(x,y), chunk);
        build_chunk(chunk.origin);

        return chunk;
    }

    private static WorldTile build_chunk_tile(int i, int j, Random chunk_random){
        int tile_id = 0;
        int height = Terrain.get_height(i,j);

        if (height > 120){
            tile_id = 25;
        }

        WorldTile tile = new WorldTile(tile_id);
                //important!
                //tile should be registered before any action is performed on it
        tile_data.put(new Point(i,j), tile);
        tile.set_height(height);

                //tile.moisture = Terrain.get_moisture(x, y);


         if (Terrain.is_lake(tile)){
             tile.set_tile_id(1);
             tile.terrain_type = TerrainType.TERRAIN_WATER;
         }
        
         if (chunk_random.nextFloat()*100<0.25f){

             EntityStone stone_ent = new EntityStone();
             EntityManager.add(stone_ent);
             stone_ent.spawn(1, new Point(i,j));

             stone_ent.set_blocking(true);
         }

        return tile;
    }

    public static void build_chunk(Point origin){

        Terrain.aquatic_tiles.clear();

        System.out.println("loading chunk @"+origin.getX()+","+origin.getY());

        NLTimer.push();

        Random chunk_random = new Random();
        chunk_random.setSeed(origin.getX()*10000+origin.getY());    //set chunk-specific seed

        //Thread.currentThread().dumpStack();
        //System.out.println("building data chunk @"+origin.toString());

        int x = origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldChunk.CHUNK_SIZE;
        
        final int OFFSET = WorldChunk.CHUNK_SIZE;
          //final int OFFSET = 0;

        /*
         * Iterate throught the chunk using offset (for smooth moisture map transition)
         *
         */

        for (int i = x - OFFSET; i<x+size+OFFSET; i++ ){
            for (int j = y - OFFSET; j<y+size+OFFSET; j++){
                //probably unnecacary
                //boolean is_acquatic = false;
                if ( i>= x && i<x+size && j >=y && j < y+size){
                    WorldTile tile = build_chunk_tile(i,j, chunk_random);
                    /*if (tile.terrain_type == TerrainType.TERRAIN_WATER){
                        is_acquatic = true;
                    }*/
                }

                /*if ( is_acquatic || Terrain.is_lake(Terrain.get_height(i, j))){
                    //System.out.println("adding aquatic tile @"+i+","+j);
                    Terrain.aquatic_tiles.add(new Point(i,j));
                }*/
                if (Terrain.is_lake(Terrain.get_height(i, j))){
                    Terrain.aquatic_tiles.add(new Point(i,j));
                }
            }
        }

        //System.out.println("calculating moisture map with "+Terrain.aquatic_tiles.size()+" aquatic tiles");

        for (int i = x; i<x+size; i++){
            for (int j = y; j<y+size; j++)
            {
                WorldTile tile = get_tile(i, j);
                tile.moisture = Terrain.get_moisture(i, j);
                tile.update_biome_type();

                if (tile.terrain_type != TerrainType.TERRAIN_WATER){
                    int biome_id = tile.biome_type.tile_id();
                    tile.set_tile_id(biome_id);
                }
                TreeGenerator.generate_object(i, j, tile, chunk_random);
                GrassGenerator.generate_object(i, j, tile, chunk_random);
                ChestGenerator.generate_object(i, j, tile, chunk_random);
            }
        }
        NLTimer.pop("chunk @"+origin.getX()+","+origin.getY());
        //System.out.println("HM Size:" + Terrain.heightmap_cached.size());
    }

    //clean all unused chunks and data
    public synchronized void chunk_gc(){

        for (Iterator<Map.Entry<Point, WorldChunk>> iter = chunk_data.entrySet().iterator();
            iter.hasNext();) {
            Map.Entry<Point, WorldChunk> entry = iter.next();
            
            WorldChunk __chunk = (WorldChunk)entry.getValue();

            if (!WorldCluster.chunk_in_cluster(__chunk.origin)){
                __chunk.unload();
                iter.remove();  
            }
        }

        //TODO: perform gc on expired tiles?
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
        WorldChunk new_chunk = get_cached_chunk(get_chunk_coord(coord_dest));
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
           WorldChunk new_chunk = get_cached_chunk(get_chunk_coord(spawn_event.ent.origin));
           
           EEntityChangeChunk e_change_chunk = new EEntityChangeChunk(spawn_event.ent,null,new_chunk);
           e_change_chunk.post();

           Point ent_origin = spawn_event.ent.origin;
           WorldTile tile_to = get_tile(ent_origin.getX(), ent_origin.getY());
           
           if (tile_to != null){
                tile_to.add_entity(spawn_event.ent);
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

           //TODO: only call on WorldCluster relocation!!!1111111
           //perform garbage collect on expired chunks
           
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
        ui.minimap.update_map();
        //minimap.update_map();
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
