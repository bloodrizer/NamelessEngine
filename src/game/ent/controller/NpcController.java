/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class NpcController extends BaseController {
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
    }

    public void follow_path(){
        if(owner.origin.getX() > destination.getX()){
            owner.move_to(new Point(owner.origin.getX()-1, owner.origin.getY()));
        }
        if(owner.origin.getX() < destination.getX()){
            owner.move_to(new Point(owner.origin.getX()+1, owner.origin.getY()));
        }
        if(owner.origin.getY() > destination.getY()){
            owner.move_to(new Point(owner.origin.getX(), owner.origin.getY()-1));
        }
        if(owner.origin.getY() < destination.getY()){
            owner.move_to(new Point(owner.origin.getX(), owner.origin.getY()+1));
        }
    }
}
