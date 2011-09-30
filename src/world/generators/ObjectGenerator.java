/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import game.GameEnvironment;
import java.util.Random;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public abstract class ObjectGenerator {
    protected GameEnvironment environment;
    
    public void setEnvironment(GameEnvironment environment){
        this.environment = environment;
    }

    public void generate_object(int x, int y, WorldTile tile, Random chunk_random){
        
    }
}
