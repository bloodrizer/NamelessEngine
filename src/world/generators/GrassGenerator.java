/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import game.ent.EntityManager;
import game.ent.decals.EntDecalGrass;
import java.util.Random;
import org.lwjgl.util.Point;
import world.WorldTile;
import world.WorldTile.TerrainType;

/**
 *
 * @author Administrator
 */
public class GrassGenerator extends ObjectGenerator {

      public static void generate_object(int x, int y, WorldTile tile, Random chunk_random){
        float rate = 0.0f;

        if (tile.terrain_type == TerrainType.TERRAIN_WATER){
            return;
        }

        switch(tile.biome_type){
            case BIOME_DECIDUOS_FOREST:
                rate = 5;
            break;

            case BIOME_TROPICAL_RAINFOREST:
                rate = 5;
            break;
            case BIOME_GRASSLAND:
                rate = 2;
            break;
            case BIOME_SHRUBLAND:
                rate = 10;
            break;
        }

        int chance = (int)(chunk_random.nextFloat()*100);
        if (chance<rate){
            add_grass(x,y);
        }
    }

    public static void add_grass(int i, int j){
        EntDecalGrass grass_ent = new EntDecalGrass();
        EntityManager.add(grass_ent);
        grass_ent.spawn(1, new Point(i,j));

        grass_ent.set_blocking(false);    //obstacle
    }

}
