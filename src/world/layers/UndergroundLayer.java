/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.layers;

import org.lwjgl.util.Point;
import world.generators.ChunkGenerator;
import world.generators.ChunkGroundGenerator;
import world.generators.ChunkUndergroundGenerator;

/**
 *
 * @author Administrator
 */
public class UndergroundLayer extends WorldLayer{

    protected static void build_chunk(Point origin, int z_index){
        ChunkGenerator underground_gen = new ChunkUndergroundGenerator();
        underground_gen.set_zindex(z_index);

        underground_gen.generate(origin);

        terrain_outdated = true;
    }

}
