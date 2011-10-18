/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import game.ent.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.Point;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class WorldChunk implements Serializable{
    //todo: synch with server?
    public static final int CHUNK_SIZE = 32;

    public Point origin = new Point(0,0);

    protected List<Entity> entList = new ArrayList<Entity>(100);
    public Map<Point,WorldTile> tile_data = new java.util.HashMap<Point,WorldTile>(1024);

    public boolean dirty = true;
    
    private WorldLayer layer = null;

    public WorldChunk(int chunk_x, int chunk_y){
        origin.setLocation(chunk_x, chunk_y);
    }
    
    public List<Entity> getEntList(){
        return entList;
    }

    public synchronized boolean add_entity(Entity ent){

        if (!entList.contains(ent)){

            entList.add(ent);
            ent.set_chunk(this);
            
            return true;

        }
        return false;
    }


    public void remove_entity(Entity ent){
            entList.remove(ent);
    }

    public void set_layer(WorldLayer layer) {
        this.layer = layer;
    }
    
    public static Point get_chunk_coord(Point position) {
        //TODO: use util point?
        int cx = (int)Math.floor((float)position.getX() / CHUNK_SIZE);
        int cy = (int)Math.floor((float)position.getY() / CHUNK_SIZE);

        return new Point(cx,cy);
    }
    
    public synchronized void unload(){
        throw new RuntimeException("Unloading method is not defined");
    }

}
