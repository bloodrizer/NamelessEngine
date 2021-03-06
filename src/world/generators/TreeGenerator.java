/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.generators;

import game.ai.BasicMobAI;
import game.ent.EntityManager;
import game.ent.controller.MobController;
import game.ent.enviroment.EntityCacti;
import game.ent.enviroment.EntityTree;
import java.util.Random;
import org.lwjgl.util.Point;
import world.WorldTile;
import world.WorldTile.BiomeType;
import world.WorldTile.TerrainType;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class TreeGenerator extends ObjectGenerator {

    public void generate_object(int x, int y, WorldTile tile, Random chunk_random){
        float tree_rate = 0.0f;

        if (tile.terrain_type == TerrainType.TERRAIN_WATER){
            return;
        }

        switch(tile.biome_type){
            case BIOME_TROPICAL_RAINFOREST:
                tree_rate = 10;
            break;

            case BIOME_SEASONAL_FOREST:
                tree_rate = 3;
            break;

            case BIOME_DECIDUOS_FOREST:
                tree_rate = 3;
            break;

            case BIOME_TEMP_RAINFOREST:
                tree_rate = 3;
            break;

            case BIOME_TAIGA:
                tree_rate = 3;
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

    public void add_tree(int i, int j){
        EntityTree tree_ent = new EntityTree();
        
        tree_ent.setLayerId(WorldLayer.GROUND_LAYER);
        
        tree_ent.setEnvironment(environment);
        tree_ent.spawn(1, new Point(i,j));
        tree_ent.set_blocking(true);    //obstacle*/
    }
    public void add_cacti(int i, int j){
        EntityCacti tree_ent = new EntityCacti();

        tree_ent.setLayerId(WorldLayer.GROUND_LAYER);

        tree_ent.setEnvironment(environment);
        tree_ent.spawn(1, new Point(i,j));
        tree_ent.set_blocking(true);    //obstacle

        //tree_ent.set_controller(new MobController());
        //tree_ent.set_ai(new BasicMobAI());*/
        
    }

}
