/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import java.util.Random;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public abstract class ObjectGenerator {
    //deprecated
    public void generate_object(int x, int y, Random chunk_random){
        
    }
    public static void generate_object(int x, int y, WorldTile tile, Random chunk_random){
        
    }
}
