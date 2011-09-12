/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.util;

import game.ent.Entity;
import game.ent.EntityManager;
import java.util.ArrayList;
import org.lwjgl.util.Point;

/**
 *
 */
public class Fov {

     public static boolean in_range(Point src, Point tgt, int range){
        int dx = src.getX() - tgt.getX();
        int dy = src.getY() - tgt.getY();

        //System.out.println("range:" + (dx*dx + dy*dy));

        if ( range*range >= dx*dx + dy*dy ){
            return true;
        }

        return false;
    }

    /*
     * This method returns every entity in radius of given point
     * Note that it is a heavy operation and you should not overuse it.
     */

    public static Entity[] get_entity_in_radius(Point origin, int range, int layer_id){
        ArrayList<Entity> list = new ArrayList<Entity>(5);

        Entity[] ents = EntityManager.getEntities(layer_id);
        for(int i=0; i< ents.length; i++){
            if(in_range(origin, ents[i].origin,range)){
                list.add(ents[i]);
            }
        }

        return list.toArray(new Entity[0]);
    }

}
