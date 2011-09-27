/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.generators;

import game.GameEnvironment;
import org.lwjgl.util.Point;



/**
 *
 * @author bloodrizer
 */
public abstract class ChunkGenerator {

    protected int z_index;
    protected GameEnvironment environment;

    public void set_zindex(int z_index){
        this.z_index = z_index;
    }

    public void setEnvironment(GameEnvironment environment){
        this.environment = environment;
    }
    
    public void generate(Point origin){
    }
}
