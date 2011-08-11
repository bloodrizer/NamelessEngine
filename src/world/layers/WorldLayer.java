/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.layers;

import java.util.Iterator;
import java.util.Map;
import org.lwjgl.util.Point;
import world.WorldChunk;
import world.WorldCluster;
import world.WorldTile;

/**
 *
 * @author bloodrizer
 */

/*
 * Layer Class for multiple terrain layers. It contains tile data and geometry

 */

public class WorldLayer {
    int z_index = 0;
    
    private static Map<Point,WorldChunk> chunk_data = new java.util.HashMap<Point,WorldChunk>(100);
    private static Map<Point,WorldTile> tile_data = new java.util.HashMap<Point,WorldTile>(1000);
    
    public void set_zindex(int zindex){
        this.z_index = zindex;
    }

    private Map<Point, WorldChunk> get_chunk_data() {
        return chunk_data;
    }
    
    public Map<Point, WorldTile> get_tile_data() {
        return tile_data;
    }
    
    public void set_chunk(Point origin, WorldChunk chunk){
        chunk_data.put(origin, chunk);
        chunk.set_layer(this);
    }

    public void gc() {
        for (Iterator<Map.Entry<Point, WorldChunk>> iter = chunk_data.entrySet().iterator();
            iter.hasNext();) {
            Map.Entry<Point, WorldChunk> entry = iter.next();
            
            WorldChunk __chunk = (WorldChunk)entry.getValue();

            if (!WorldCluster.chunk_in_cluster(__chunk.origin)){
                __chunk.unload();
                iter.remove();  
            }
        }
    }

    public WorldChunk get_chunk(Point util_point) {
        return chunk_data.get(util_point);
    }
}
