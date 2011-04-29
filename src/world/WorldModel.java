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
import java.util.Collections;
import java.util.Iterator;
import org.lwjgl.util.Point;
import player.Player;

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

    private static synchronized WorldTile get_tile(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);
        
        WorldTile tile = tile_data.get(util_point);
        pop_point(util_point);

        return tile;
    }
    //--------------------------------------------------------------------------
    private static java.util.Map<Point,WorldChunk> chunk_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldChunk>(100));

    public static int cluster_size = 3;
   
    private static synchronized WorldChunk get_chunk(Point location){
        return get_chunk(location.getX(),location.getY());
    }
    private static synchronized WorldChunk get_chunk(int x, int y){
        //push_point(util_point);
        //util_point.setLocation(x, y);

        //WorldChunk chunk = chunk_data.get(util_point);
        WorldChunk chunk = chunk_data.get(new Point(x,y));
        //pop_point(util_point);

        return chunk;
    }

    public static Point get_chunk_coord(Point position) {
        //todo: use util point?
        int CL_OFFSET = (cluster_size-1)/2;

        int cx = (int)Math.floor(position.getX() / WorldChunk.CHUNK_SIZE)-CL_OFFSET;
        int cy = (int)Math.floor(position.getY() / WorldChunk.CHUNK_SIZE)-CL_OFFSET;

        return new Point(cx,cy);
    }

    public static void precache_chunk(int chunk_x, int chunk_y){
        WorldChunk chunk = get_chunk(chunk_x, chunk_y);
        if (chunk != null){
            //do nothing
        }
        else{
            chunk = new WorldChunk(chunk_x,chunk_y);

        }
    }


    public WorldModel(){
        EventManager.subscribe((IEventListener) this);
    }

    private long timer = 0; //todo: synchronize me

    public void update(){
        //1. update timer data
        //2. check if think call is allowed
        //3. call think
        Timer.tick();


        for (Iterator iter = EntityManager.ent_list_sync.iterator(); iter.hasNext();) {
           Entity entity = (Entity) iter.next();
           if (entity.is_awake(Timer.get_time())){
              entity.think();
           }
        }
    }

   















    public static synchronized void move_entity(Entity entity, Point dest){
        //System.out.println("world model::on entity move to:"+dest.toString());
        entity.origin.setLocation(dest);


        
        entity.origin.setLocation(dest);

        //now with a chunk shit
        //----------------------------------------------------------------------
        WorldChunk new_chunk = get_chunk(get_chunk_coord(dest));
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
        //----------------------------------------------------------------------
    }



    //----------------------------EVENTS SHIT-----------------------------------
    public void e_on_event(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;
           move_entity(move_event.entity, move_event.getTo());
       }
       else if(event instanceof EEntitySpawn){
           EEntitySpawn spawn_event = (EEntitySpawn)event;
           //-------------------------------------------------------------------
           WorldChunk new_chunk = get_chunk(get_chunk_coord(spawn_event.ent.origin));
           
           EEntityChangeChunk e_change_chunk = new EEntityChangeChunk(spawn_event.ent,null,new_chunk);
           e_change_chunk.post();
           //spawn_event.ent.origin = spawn_event.origin;

       }else if(event instanceof EEntityChangeChunk){
           EEntityChangeChunk e_change_chunk = (EEntityChangeChunk)event;
           //System.out.println("setting new chunk for ent "+Entity.toString(e_change_chunk.ent));
           
           e_change_chunk.ent.set_chunk(e_change_chunk.to);
       }


    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
       if (event instanceof EEntityMove){
           EEntityMove move_event = (EEntityMove)event;
           move_entity(move_event.entity, move_event.getFrom());
       }
    }
}
