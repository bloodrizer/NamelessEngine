/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import game.ent.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class WorldChunk {
    //todo: synch with server?
    public static final int CHUNK_SIZE = 32;

    public Point origin = new Point(0,0);

    private List<Entity> ent_list = new ArrayList<Entity>(100);

    public boolean dirty = true;

    public WorldChunk(int chunk_x, int chunk_y){
        origin.setLocation(chunk_x, chunk_y);
    }

    public synchronized boolean add_entity(Entity ent){

        if (!ent_list.contains(ent)){

            ent_list.add(ent);
            ent.set_chunk(this);
            
            return true;

        }
        return false;
    }

    public synchronized void unload(){

        System.out.println("unloading chunk @"+origin.toString());
        System.out.println("trying to remove " + Integer.toString( ent_list.size() ) +" entities");

   
        for (Iterator iter = ent_list.iterator(); iter.hasNext();) {
                Entity ent = (Entity) iter.next();
                EntityManager.remove_entity(ent);
                //remove_entity(ent);
                iter.remove();
        }

        System.out.println(Integer.toString( ent_list.size() ) +" entities left");
    }

    public void remove_entity(Entity ent){
            ent_list.remove(ent);
    }
}
