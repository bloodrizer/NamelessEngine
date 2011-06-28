/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import java.util.Random;

/**
 *
 * @author Administrator
 */
public interface ObjectGenerator {
    public void generate_object(int x, int y, Random chunk_random);
}
