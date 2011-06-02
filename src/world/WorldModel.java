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
import game.ent.EntityStone;
import game.ent.enviroment.EntityTree;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.util.Point;
import player.Player;
import world.WorldTile.TerrainType;
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

    //--------------------------------------------------------------------------
    private static java.util.Map<Point,WorldChunk> chunk_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldChunk>(100));

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


        Object[] list = EntityManager.ent_list_sync.toArray();
        for(int i=EntityManager.ent_list_sync.size()-1; i>=0; i--){
            Entity entity = (Entity)list[i];
            if (entity.is_awake(Timer.get_time())){
                  entity.think();
            }
            if (entity.is_next_frame(Timer.get_time())){
                  entity.next_frame();
            }
        }
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

    public static void build_chunk(Point origin){

        //Thread.currentThread().dumpStack();
        //System.out.println("building data chunk @"+origin.toString());

        int x = origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldChunk.CHUNK_SIZE;

        for (int i = x; i<x+size; i++)
            for (int j = y; j<y+size; j++)
            {
                //implement different tile_id there
                //int tile_id = (int)(Math.random()*10);

                int tile_id = 0;
                /*if (Math.random() < 0.2f){
                    tile_id = 25;
                }*/
                int height = Terrain.get_height(i,j);
                if (height > 120){
                    tile_id = 25;
                }

                WorldTile tile = new WorldTile(tile_id);
                //important!
                //tile should be registered before any action is performed on it
                tile_data.put(new Point(i,j), tile);

                tile.set_height(height);

                if (Terrain.is_tree(tile)){
                    //tile.set_tile_id(50);   //debug only!

                    EntityTree tree_ent = new EntityTree();
                    EntityManager.add(tree_ent);
                    tree_ent.spawn(1, new Point(i,j));
                    
                    tree_ent.set_blocking(true);    //obstacle
                }

                if (Terrain.is_lake(tile)){
                    tile.set_tile_id(1);
                    tile.terrain_type = TerrainType.TERRAIN_WATER;
                }

                if (Math.random()*100<0.25f){
                    EntityStone stone_ent = new EntityStone();
                    EntityManager.add(stone_ent);
                    stone_ent.spawn(1, new Point(i,j));

                    stone_ent.set_blocking(true);
                }

                
            }
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


           //spawn_event.ent.origin = spawn_event.origin;
           //-------------------------------------------------------------------
       }else if(event instanceof EEntityChangeChunk){
           EEntityChangeChunk e_change_chunk = (EEntityChangeChunk)event;

           //System.err.println("setting new chunk @ for ent "+Entity.toString(e_change_chunk.ent));
           
           Entity ent = e_change_chunk.ent;
           //ent.set_chunk(e_change_chunk.to);
           e_change_chunk.to.add_entity(ent);

           if (ent.isPlayerEnt()){
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
