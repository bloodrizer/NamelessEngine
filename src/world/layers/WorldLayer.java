/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.layers;

import events.EEntityChangeChunk;
import game.ent.Entity;
import game.ent.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import ne.Game;
import org.lwjgl.util.Point;
import ui.GameUI;
import world.Terrain;
import world.Timer;
import world.WorldChunk;
import world.WorldCluster;
import world.WorldModel;
import world.WorldRegion;
import world.WorldTile;
import world.generators.ChunkGenerator;
import world.generators.ChunkGroundGenerator;
import world.generators.NPCVillageGenerator;
import world.util.astar.Mover;
import world.util.astar.TileBasedMap;

/**
 *
 * @author Administrator
 */

/*
 * This class handles world terrain data, including landscape, moisture, lightning model, object distribution, minerals, etc.
 */
public class WorldLayer{


    public static final int GROUND_LAYER = 0;

    private int z_index;

    static final int MAP_SIZE = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;
    public WorldModelTileMap tile_map = new WorldModelTileMap(this);


    
    //--------------------------------------------------------------------------
    private Point util_point     = new Point(0,0);
    private Point __stack_point  = new Point(0,0);

    private static boolean light_outdated = false;  //shows if model should rebuild terrain lightning
    protected static boolean terrain_outdated = false;  //shows if model should rebuild terrain lightning

    //--------------------------------------------------------------------------

    private Map<Point,WorldChunk> chunk_data = new java.util.HashMap<Point,WorldChunk>(100);
    public Map<Point,WorldTile> tile_data = new java.util.HashMap<Point,WorldTile>(1000);



    
    /*public static WorldLayer get_layer(int layer_id){
        WorldLayer layer = world_layers.get(layer_id);
        return layer;
    }*/

    
    public void set_tile(Point origin, WorldTile tile){
        tile_data.put(origin, tile);
    }

    public static void invalidate_light(){
        light_outdated = true;
    }

    //todo: use actual stack there
    private void push_point(Point point){
        __stack_point.setLocation(point);
    }
    private void pop_point(Point point){
        point.setLocation(__stack_point);
    }

    public WorldTile get_tile(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);
        
        WorldTile tile = tile_data.get(util_point);
        pop_point(util_point);

        return tile;
    }
    
    /*public static synchronized WorldTile get_tile(int x, int y){
        return get_tile(x,y, GROUND_LAYER);
    }*/

    //DEPRECATED DEPRECATED DEPRECATED
    public static boolean tile_blocked(Point tile_origin){
        return false;
    }

    private WorldChunk get_chunk(Point location){
        return chunk_data.get(location);
    }
    
    private WorldChunk get_chunk(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);

        WorldChunk chunk = get_chunk(util_point);
        //WorldChunk chunk = chunk_data.get(new Point(x,y));
        pop_point(util_point);

        return chunk;
    }

    public WorldChunk get_cached_chunk(int chunk_x, int chunk_y){
        WorldChunk chunk = get_chunk(chunk_x, chunk_y);
        if (chunk == null){
            chunk = precache_chunk(chunk_x, chunk_y);
        }
        return chunk;
    }

    public WorldChunk get_cached_chunk(Point location){
        return get_cached_chunk(location.getX(),location.getY());
    }

    public void update(){
        
        ArrayList<Entity> entList = EntityManager.getList(z_index);
        Object[] entArray = entList.toArray();
        
        for(int i=entList.size()-1; i>=0; i--){
            Entity entity = (Entity)entArray[i];

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

        if (terrain_outdated){
            update_terrain();
            terrain_outdated = false;
        }
        
    }

    public void recalculate_light(){


        Object[] ent_list = EntityManager.getList(z_index).toArray();

        int x = WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;

        for (int i = x; i<x+size; i++)
        for (int j = y; j<y+size; j++){
             int chunk_x = (int)Math.floor((float)i / WorldChunk.CHUNK_SIZE);
             int chunk_y = (int)Math.floor((float)j / WorldChunk.CHUNK_SIZE);

             if (get_cached_chunk(
                        chunk_x,
                        chunk_y) != null)
             {
                 WorldTile tile = get_tile(i,j);

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

    private WorldChunk precache_chunk(int x, int y){
        WorldChunk chunk = new WorldChunk(x, y);

        chunk_data.put(new Point(x,y), chunk);
        process_chunk(chunk.origin, z_index);

        return chunk;
    }



    public static void process_chunk(Point origin, int z_index){
        build_chunk(origin, z_index);

        /*
         * TODO: We can not simply load one region player is into,
         * since the map will look dull and empty
         *
         * We should load large portion of regions, at least 3x3 blocks
         */

        int rx = origin.getX()/WorldRegion.REGION_SIZE;
        int ry = origin.getY()/WorldRegion.REGION_SIZE;
        Point regionOrigin = new Point(rx,ry);
        WorldModel.worldRegions.get_cached(regionOrigin);
        
    }

    protected static void build_chunk(Point origin, int z_index){
        ChunkGenerator ground_gen = new ChunkGroundGenerator();
        ground_gen.set_zindex(z_index);
        ground_gen.generate(origin);

        /*
         * Generate village if region belongs to NPC
         */

        NPCVillageGenerator village_gen = new NPCVillageGenerator();
        village_gen.set_zindex(z_index);
        village_gen.generate(origin);

        terrain_outdated = true;
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
    }




    public void move_entity(Entity entity, Point coord_dest){
        Point coord_from = new Point(entity.origin);  //defence copy
        
        //System.out.println("world model::on entity move to:"+coord_dest.toString());
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

    /*
     * update_terrain is called whenever player_ent crosses a border of terrain
     and new portion of terrain generation is required
     *
     */

    public void update_terrain() {
        Terrain.heightmap_cached.clear();
        //System.out.println("clearing aquatic tiles data");
        //Terrain.aquatic_tiles.clear();

        GameUI ui = (GameUI)(Game.get_game_mode().get_ui());
        
        if(ui.minimap != null){
            ui.minimap.expired = true;
            ui.minimap.update_map();
        }
    }

    public void set_zindex(int z_index) {
        this.z_index = z_index;
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

        WorldLayer layer = null;
        public WorldModelTileMap(WorldLayer layer){
            this.layer = layer;
        }

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

            WorldTile tile = layer.get_tile(temp.getX(), temp.getY());
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
