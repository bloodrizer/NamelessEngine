/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import ne.Game;
import org.lwjgl.util.Point;
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

    public void follow_path(){
        Point __destination = new Point(this.destination);
        

        if (path!=null && path.getLength() > 0){
            
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


            Step step = path.popStep();

            Point location = new Point(step.getX(),step.getY());
            location = WorldModel.tile_map.local2world(location);

            __destination.setLocation(location.getX(), location.getY());
        }

        if(owner.origin.getX() > __destination.getX()){
            owner.move_to(new Point(owner.origin.getX()-1, owner.origin.getY()));
        }
        if(owner.origin.getX() < __destination.getX()){
            owner.move_to(new Point(owner.origin.getX()+1, owner.origin.getY()));
        }
        if(owner.origin.getY() > __destination.getY()){
            owner.move_to(new Point(owner.origin.getX(), owner.origin.getY()-1));
        }
        if(owner.origin.getY() < __destination.getY()){
            owner.move_to(new Point(owner.origin.getX(), owner.origin.getY()+1));
        }

        if(owner.origin.equals(destination)){
            this.destination = null;    //clean up destination
        }
    }
}
