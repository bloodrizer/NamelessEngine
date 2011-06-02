/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import events.network.EEntitySetPath;
import game.ent.Entity.Orientation;
import org.lwjgl.util.Point;
import render.NPCRenderer;
import world.Timer;
import world.WorldModel;
import world.util.astar.AStarPathFinder;
import world.util.astar.Mover;
import world.util.astar.Path;
import world.util.astar.Path.Step;

/**
 *
 * @author Administrator
 */
public class NpcController extends BaseController implements Mover {

    public Point destination = null;
    public static final int SYNCH_CHUNK_SIZE = 1;
    
    public Path path = null;
    public Step step = null;


    int path_synch_counter = 0;

    @Override
    public void think() {
        if (destination != null){

            owner.set_next_frame(100);

            follow_path();
            return;
        }

        owner.set_next_frame(200000);
    }

    public void set_destination(Point destination){

        path_synch_counter = 0; //reset synchronisation counter

        owner.next_frame = Timer.get_time();

        this.destination = destination;
        
        //path.calculate(destination)
        calculate_path(
                destination.getX(),
                destination.getY()
        );
    }



    private static AStarPathFinder finder = new AStarPathFinder(
            WorldModel.tile_map,
            50,
            false
   );

    public void calculate_path(int x, int y){

        Point target = new Point(x,y);
        target = WorldModel.tile_map.world2local(target);

        Point source = new Point(owner.origin);
        source = WorldModel.tile_map.world2local(source);


        //WorldModel.clearVisited();
        path = finder.findPath(this,
            source.getX(), source.getY(), target.getX(), target.getY());
        step = null;
        /*if (path.steps!= null && path.steps.size() > 0 ){
            path.steps.remove(0);
        }*/
    }

    public void change_tile(int x, int y){
        owner.dx = 0.0f;
        owner.dy = 0.0f;
        /// owner.origin.setLocation(x, y);
        owner.move_to(new Point(x,y));

        step = null;

        //------------path synchronisation-----------
        System.out.println("changing tile");
        if (owner.isPlayerEnt() && path != null){
            System.out.println("path counter:"+path_synch_counter);
            if (path_synch_counter == 0){    //we are in the point of
                Point __dest;
                Step __step;

                if (path.getLength()>0){
                    //extract checkpoint step
                    if (path.getLength()>SYNCH_CHUNK_SIZE){
                        __step = path.getStep(SYNCH_CHUNK_SIZE);
                    }else{
                        __step = path.getStep(path.getLength()-1);
                    }
                }else{
                    __step = step;  //load pushed step;
                }

                __dest = new Point(__step.getX(),__step.getY());
                __dest = WorldModel.tile_map.local2world(__dest);

                System.out.println("sending step [" +
                        __step +
                        "] w2l ["+ __dest +
                        "] (length:" + path.getLength() + ")"
                );

                System.out.println(path);


                

                EEntitySetPath dest_event = new EEntitySetPath(owner, __dest);
                dest_event.post();
            }
            path_synch_counter++;
            if (path_synch_counter>=SYNCH_CHUNK_SIZE){
                path_synch_counter = 0;
            }

        }
        //--------------------------------------------

    }
    public void move_ent(int x, int y){

        //wachky-hacky safe switch
        if(WorldModel.get_tile(x, y).is_blocked()){
            step = null;
            path = null;
            return;
        }

        //owner.move_to(new Point(owner.origin.getX()-1, owner.origin.getY()));

        //displacement = 1.0f / (owner.get_renderer().ANIMATION_LENGTH-2)   //1 start frame + 1 end frame + iterated animation
        float dx = (float)(x-owner.origin.getX())*0.1f;
        float dy = (float)(y-owner.origin.getY())*0.1f;

        owner.dx += dx;
        owner.dy += dy;

        //todo: use single % counter to maisure if to change tile or not

        if (owner.dx > 1.0f || owner.dx < -1.0f){
            change_tile(x,y);
            return;
        }
        if (owner.dy > 1.0f || owner.dy < -1.0f){
            change_tile(x,y);
            return;
        }
         //owner.origin.setLocation(x, y);
         //step = null;
    }


    public void follow_path(){
        Point __destination = new Point(this.destination);

        if (path!=null && path.getLength() > 1){


            if(step == null || step.equals(owner.origin)){  //this is safe hack, that hides the 'ent-lock' glitch
                step = path.popStep();
            }

            Point location = new Point(step.getX(),step.getY());
            location = WorldModel.tile_map.local2world(location);

            

            __destination.setLocation(location.getX(), location.getY());
        }

        if(owner.origin.getX() > __destination.getX()){
            move_ent(owner.origin.getX()-1, owner.origin.getY());
            owner.orientation = Orientation.ORIENT_W;
        }
        if(owner.origin.getX() < __destination.getX()){
            move_ent(owner.origin.getX()+1, owner.origin.getY());
            owner.orientation = Orientation.ORIENT_E;
        }
        if(owner.origin.getY() > __destination.getY()){
            move_ent(owner.origin.getX(), owner.origin.getY()-1);
            owner.orientation = Orientation.ORIENT_N;
        }
        if(owner.origin.getY() < __destination.getY()){
            move_ent(owner.origin.getX(), owner.origin.getY()+1);
            owner.orientation = Orientation.ORIENT_S;
        }

        if(owner.origin.equals(destination)){
            this.destination = null;    //clean up destination
            step = null;                //this is required too
            path = null;
            ((NPCRenderer)owner.get_render()).set_frame(0);
        }
    }
}
