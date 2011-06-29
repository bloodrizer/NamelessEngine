/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import game.ent.EntityManager;
import game.ent.enviroment.EntityCacti;
import game.ent.enviroment.EntityTree;
import java.util.Random;
import org.lwjgl.util.Point;
import world.WorldTile;
import world.WorldTile.BiomeType;
import world.WorldTile.TerrainType;

/**
 *
 * @author Administrator
 */
public class TreeGenerator extends ObjectGenerator {

    public static void generate_object(int x, int y, WorldTile tile, Random chunk_random){
        float tree_rate = 0.0f;

        if (tile.terrain_type == TerrainType.TERRAIN_WATER){
            return;
        }

        switch(tile.biome_type){
            case BIOME_TROPICAL_RAINFOREST:
                tree_rate = 30;
            break;

            case BIOME_SEASONAL_FOREST:
                tree_rate = 10;
            break;

            case BIOME_DECIDUOS_FOREST:
                tree_rate = 10;
            break;

            case BIOME_TEMP_RAINFOREST:
                tree_rate = 10;
            break;

            case BIOME_TAIGA:
                tree_rate = 10;
            break;
            
            case BIOME_GRASSLAND:
                tree_rate = 1;
            break;

            case BIOME_TEMP_DESERT:
                tree_rate = 2;
            break;

            case BIOME_SUBTROPICAL_DESERT:
                tree_rate = 1;
            break;
        }

        int chance = (int)(chunk_random.nextFloat()*100);
        if (chance<tree_rate){
            if (tile.biome_type == BiomeType.BIOME_TEMP_DESERT ||
                    tile.biome_type == BiomeType.BIOME_SUBTROPICAL_DESERT){
                add_cacti(x,y);
            }else{
                add_tree(x,y);
            }
        }
    }

    public static void add_tree(int i, int j){
        EntityTree tree_ent = new EntityTree();
        EntityManager.add(tree_ent);
        tree_ent.spawn(1, new Point(i,j));

        tree_ent.set_blocking(true);    //obstacle
    }
    public static void add_cacti(int i, int j){

        EntityCacti tree_ent = new EntityCacti();
        EntityManager.add(tree_ent);
        tree_ent.spawn(1, new Point(i,j));

        tree_ent.set_blocking(true);    //obstacle
    }

}
