/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import game.ent.Entity.Orientation;
import ne.Game;
import org.lwjgl.util.Point;
import render.NPCRenderer;
import world.WorldModel;
import world.util.astar.AStarPathFinder;
import world.util.astar.Mover;
import world.util.astar.Path;
import world.util.astar.Path.Step;
import world.util.astar.PathFinder;

/**
 *
 * @author Administrator
 */
public class NpcController extends BaseController implements Mover {

    private Point destination = null;

    @Override
    public void think() {
        if (destination != null){
            //blah
            //owner.move_to(destination);
            follow_path();
        }
        //owner.sleep(50);
    }

    public void set_destination(Point destination){

        this.destination = destination;
        //path.calculate(destination)
        calculate_path(
                destination.getX(),
                destination.getY()
        );
    }

    Path path = null;
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
    }

    public void change_tile(int x, int y){
        owner.dx = 0.0f;
        owner.dy = 0.0f;
        /// owner.origin.setLocation(x, y);
        owner.move_to(new Point(x,y));

        step = null;
    }
    public void move_ent(int x, int y){

        //wachky-hacky safe switch
        if(WorldModel.get_tile(x, y).is_blocked()){
            step = null;
            path = null;
            return;
        }

        ((NPCRenderer) owner.get_render()).next_frame();

        //owner.move_to(new Point(owner.origin.getX()-1, owner.origin.getY()));

        //displacement = 1.0f / (owner.get_renderer().ANIMATION_LENGTH-2)   //1 start frame + 1 end frame + iterated animation
        float dx = (float)(x-owner.origin.getX())*0.2f;
        float dy = (float)(y-owner.origin.getY())*0.2f;

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


    Step step = null;
    public void follow_path(){
        Point __destination = new Point(this.destination);
        

        if (path!=null && path.getLength() > 1){
            
            /*for(int i = 0; i < path.getLength(); i++){
                //--------------------------------------------------------------
                
                Step tmp_step = path.getStep(i);
                Point tmp = new Point(tmp_step.getX(),tmp_step.getY());
                tmp = WorldModel.tile_map.local2world(tmp);
                //--------------------------------------------------------------
                
                System.out.println("path step #"+
                        Integer.toString(i)
                        +tmp.toString());
            }*/

            if(step == null){
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
        }
    }
}
