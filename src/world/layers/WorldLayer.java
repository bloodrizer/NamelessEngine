/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.layers;

import java.util.Map;
import org.lwjgl.util.Point;
import world.WorldChunk;

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
    
    public void set_zindex(int zindex){
        this.z_index = zindex;
    }

    public Map<Point, WorldChunk> get_chunk_data() {
        return chunk_data;
    }
}
