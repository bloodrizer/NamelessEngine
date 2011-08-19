/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world;

import org.lwjgl.util.Point;

/**
 *
 * @author bloodrizer
 */

/*
 * Simple static helper class, that handles region mechanics
 * 
 * that includes village ownership, totem status, etc.
 * 
 */
public class WorldRegion {
    public static int REGION_SIZE = 5;
    
    /*
     * Returns region coord based on the region tile coord.
     * (0,0) tile equivalent to (0,0) chunk and simillary to the (0,0) region
     */
    
    public static Point get_region_coord(Point tile_coord){
        Point chunk_coord = WorldChunk.get_chunk_coord(tile_coord);
        int rx = chunk_coord.getX()/REGION_SIZE;
        int ry = chunk_coord.getY()/REGION_SIZE;
        
        chunk_coord.setLocation(rx, ry);    
        /*
         * we probably do not need to use defensive copyng there since
         * get_chunk_coord creates safe object anyway
         */
        return chunk_coord;
    }
}
