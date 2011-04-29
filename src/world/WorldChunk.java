/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class WorldChunk {
    //todo: synch with server?
    public static final int CHUNK_SIZE = 64;

    private List<Entity> ent_list = new ArrayList<Entity>(100);

    public WorldChunk(int chunk_x, int chunk_y){
        
    }

    public synchronized boolean add_entity(Entity ent){

        if (!ent_list.contains(ent)){

            ent_list.add(ent);
            ent.set_chunk(this);
            
            return true;

        }
        return false;
    }

    public void remove_entity(Entity ent){
            ent_list.remove(ent);
    }
}
