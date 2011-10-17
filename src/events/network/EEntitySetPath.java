/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import game.ent.Entity;
import org.lwjgl.util.Point;
import world.layers.WorldLayer;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public class EEntitySetPath extends NetworkEvent {
    private Point from = new Point();
    private Point to   = new Point();
    public Entity entity = null;

    public Point getFrom(){
        return new Point(from);
    }

    public Point getTo(){
        return new Point(to);
    }

    public EEntitySetPath(Entity entity, Point target){
        this.entity = entity;
        this.from.setLocation(entity.origin);
        this.to.setLocation(target);
    }

    public static String toString(EEntitySetPath event){
        return "[#"+Integer.toString(event.get_eventid())+"[EEntitySetPath] from @"+event.from.toString()+" to @"+event.to.toString()+"]";
    }

    //safe switch - do not allow to move at the place that is blocked
    //TODO: recalculate astar path by posting new event

    @Override
    public void post(){
        WorldTile tile = entity.getLayer().get_tile(this.to.getX(), this.to.getY());
        if (tile!=null && !tile.is_blocked()){
            super.post();
        }else{
            System.out.println("target tile @ "+this.to+" is blocked, discarding");
        }
    }

    @Override
    public String toString(){
        return toString(this);
    }

    @Override
    public String get_id(){
        return "0x260";

    }

    @Override
    public String[] serialize(){
        return new String[] {
            get_id(),
            //"2",    //meens movement with destination
            Integer.toString(this.to.getX()),
            Integer.toString(this.to.getY())
        };
    }
}
