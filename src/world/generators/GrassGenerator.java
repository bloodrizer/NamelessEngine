/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import game.ent.EntityManager;
import game.ent.decals.EntDecalFlower;
import game.ent.decals.EntDecalGrass;
import java.util.Random;
import org.lwjgl.util.Point;
import world.WorldTile;
import world.WorldTile.TerrainType;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class GrassGenerator extends ObjectGenerator {

      public void generate_object(int x, int y, WorldTile tile, Random chunk_random){
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
            if ( Math.random()*100 < 5 ){ //5% chance of spawning flower
                add_flower(x,y);
            }else{
                add_grass(x,y);
            }
        }
    }

    public void add_grass(int i, int j){
        EntDecalGrass grass_ent = new EntDecalGrass();
        grass_ent.setEnvironment(environment);
        grass_ent.spawn(1, new Point(i,j));

        grass_ent.set_blocking(false);    //obstacle
    }
    public void add_flower(int i, int j){

        EntDecalFlower flower_ent = new EntDecalFlower();
        flower_ent.setEnvironment(environment);
        flower_ent.spawn(1, new Point(i,j));
        flower_ent.set_blocking(false);    //obstacle
    }

}
