/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.Event;
import game.ent.Entity;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
@NetID(id=1)
public class EEntityMove extends NetworkEvent {
    private Point from = new Point();
    private Point to   = new Point();
    public Entity entity = null;

    public Point getFrom(){
        return new Point(from);
    }

    public Point getTo(){
        return new Point(to);
    }

    public EEntityMove(Entity entity, Point target){
        this.entity = entity;
        this.from.setLocation(entity.origin);
        this.to.setLocation(target);
    }

    public static String toString(EEntityMove event){
        return "[#"+Integer.toString(event.get_eventid())+"[EEntityMove] from @"+event.from.toString()+" to @"+event.to.toString()+"]";
    }

    @Override
    public String toString(){
        return toString(this);
    }
}
