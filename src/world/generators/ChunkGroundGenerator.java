/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.generators;

import game.ent.EntityManager;
import game.ent.enviroment.EntityStone;
import java.util.Random;
import org.lwjgl.util.Point;
import world.Terrain;
import world.WorldChunk;
import world.WorldModel;
import world.WorldTile;
import world.WorldTile.TerrainType;
import world.util.NLTimer;

/**
 *
 * @author bloodrizer
 */
public class ChunkGroundGenerator extends ChunkGenerator {
    public void generate(Point origin){
        Terrain.aquatic_tiles.clear();

        System.out.println("loading chunk @"+origin.getX()+","+origin.getY());

        NLTimer.push();

        Random chunk_random = new Random();
        chunk_random.setSeed(origin.getX()*10000+origin.getY());    //set chunk-specific seed

        //Thread.currentThread().dumpStack();
        //System.out.println("building data chunk @"+origin.toString());

        int x = origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldChunk.CHUNK_SIZE;
        
        final int OFFSET = WorldChunk.CHUNK_SIZE;
          //final int OFFSET = 0;

       //---------------------------------------------------------------------

        //Step 1. Generate heightmap
         
        /*
         * Iterate throught the chunk using offset (for smooth moisture map transition)
         * Store all aquatic-type tiles in temp array so we could quickly iterate them later
         */

        for (int i = x - OFFSET; i<x+size+OFFSET; i++ ){
            for (int j = y - OFFSET; j<y+size+OFFSET; j++){
                if ( i>= x && i<x+size && j >=y && j < y+size){
                    WorldTile tile = build_chunk_tile(i,j, chunk_random);
                }

                if (Terrain.is_lake(Terrain.get_height(i, j))){
                    Terrain.aquatic_tiles.add(new Point(i,j));
                }
            }
        }
        //---------------------------------------------------------------------
        //Step 2. Generate moisture map and biomes
        /*
         * Calculate tile moisture map based on distance from aqatic tiles
         * Assign biome type based on moisture amt and elevation
         * 
         * Assign various ents (Trees, grass, etc) based on biome type
         */
        //---------------------------------------------------------------------

        //9k iterations
        for (int i = x; i<x+size; i++){
            for (int j = y; j<y+size; j++)
            {
                WorldTile tile = WorldModel.get_tile(i, j, z_index);
                tile.moisture = Terrain.get_moisture(i, j);
                tile.update_biome_type();

                if (tile.terrain_type != TerrainType.TERRAIN_WATER){
                    int biome_id = tile.biome_type.tile_id();
                    tile.set_tile_id(biome_id);
                }
                //TODO: this part probably need some future refactoring
                TreeGenerator.generate_object(i, j, tile, chunk_random);
                StoneGenerator.generate_object(i, j, tile, chunk_random);
                GrassGenerator.generate_object(i, j, tile, chunk_random);
                ChestGenerator.generate_object(i, j, tile, chunk_random);
            }
        }

        //---------------------------------------------------------------------



        NLTimer.pop("chunk @"+origin.getX()+","+origin.getY());
        //System.out.println("HM Size:" + Terrain.heightmap_cached.size());

        //Step 3. Generate transition map for smooth biomes borders
        //---------------------------------------------------------------------

        //82k iterations
        for (int i = x+1; i<x+size-1; i++){
            for (int j = y+1; j<y+size-1; j++)
            {
                WorldTile ref_tile = WorldModel.get_tile(i, j, z_index);

                for (int k = i-1; k<i+1; k++){
                    for (int l = j-1; l<j+1; k++){
                        if(k==i||l==j){ return; }

                        WorldTile nb_tile = WorldModel.get_tile(k, l, z_index);
                        if (ref_tile.biome_type.get_zindex() > nb_tile.biome_type.get_zindex()){
                            ///save this shit
                        }
                    }
                }
                /*
                 * pseudocode:
                 *
                 * get n,s,w,e,ns,ne,ws,we
                 * calculate transition type index based on tile z-order and biome z-order
                 * todo: implement BiomeType.get_zindex();
                 *
                 * assign index, so we could apply mask later
                 */
            }
        }
    }
    
    //--------------------------------------------------------------------------
    private WorldTile build_chunk_tile(int i, int j, Random chunk_random){
        int tile_id = 0;
        int height = Terrain.get_height(i,j);

        if (height > 120){
            tile_id = 25;
        }

        WorldTile tile = new WorldTile(tile_id);
                //important!
                //tile should be registered before any action is performed on it
        WorldModel.set_tile(new Point(i,j), tile, z_index);
        tile.set_height(height);

         if (Terrain.is_lake(tile)){
             tile.set_tile_id(1);
             tile.terrain_type = TerrainType.TERRAIN_WATER;
         }

        return tile;
    }
}
