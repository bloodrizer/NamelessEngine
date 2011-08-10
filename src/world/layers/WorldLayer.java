/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.layers;

import java.util.Map;
import org.lwjgl.util.Point;
import world.WorldChunk;
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

    public Map<Point, WorldChunk> get_chunk_data() {
        return chunk_data;
    }
    
    public Map<Point, WorldTile> get_tile_data() {
        return tile_data;
    }
}
