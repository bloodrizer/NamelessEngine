/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.generators;

import game.ent.EntityManager;
import game.ent.enviroment.EntityStone;
import java.util.Random;
import org.lwjgl.util.Point;
import world.WorldTile;
import world.layers.WorldLayer;

/**
 *
 * @author dpopov
 */
public class StoneGenerator extends ObjectGenerator {
    public static void generate_object(int x, int y, WorldTile tile, Random chunk_random){
         if (chunk_random.nextFloat()*100<0.25f){

             /*EntityStone stone_ent = new EntityStone();
             EntityManager.add(stone_ent, WorldLayer.GROUND_LAYER);
             stone_ent.spawn(new Point(x,y));

             stone_ent.set_blocking(true);*/
         }
    }
    
}
