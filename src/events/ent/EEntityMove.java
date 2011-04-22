/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.ent;

import events.Event;
import game.ent.Entity;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class EEntityMove extends Event {
    public Point from = null;
    public Point to = null;
    public Entity entity = null;

    public EEntityMove(Entity entity, Point target){
        this.entity = entity;
        this.from = entity.origin;
        this.to = target;
    }
}
