/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.Event;
import game.ent.Entity;
import org.lwjgl.util.Point;
import player.Player;
import world.layers.WorldLayer;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
@NetID(id="")
public class EEntityMove extends Event {
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

    //safe switch - do not allow to move at the place that is blocked
    //TODO: recalculate astar path by posting new event

    @Override
    public void post(){

        WorldLayer layer = entity.getLayer();

        WorldTile tile = layer.get_tile(this.to.getX(), this.to.getY());
        if (tile!= null && !tile.is_blocked()){
            super.post();
        }
        //TODO: fix server bug when tile is null
    }

    @Override
    public String toString(){
        return toString(this);
    }

    /*@Override
    public String get_id(){
        if (this.entity == Player.get_ent()){
            return "0x0260";
        }
        return "0x0280";
        
    }*/

    /*@Override
    public String[] serialize(){
        return new String[] {
            get_id(),
            Integer.toString(this.to.getX()),
            Integer.toString(this.to.getY())
        };
    }*/
}
