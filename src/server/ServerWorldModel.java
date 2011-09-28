/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import world.WorldModel;
import world.layers.WorldLayer;


/**
 *
 * @author Administrator
 *
 *
 * Server's WorldModel. Can save and load in-game data
 *
 */

public class ServerWorldModel extends WorldModel{

    public ServerWorldModel(){

        //do some sql lite initialization there
        for (int i = 0; i< LAYER_COUNT; i++ ){
            WorldLayer layer = new WorldLayer(){
                @Override
                protected void build_chunk(Point origin, int z_index){
                        //TODO: implement server-side chunk generation

                        //TODO: implement server-side chunk generation
   
                        //2. If not, perform generation cycle on it and create necesery entities

                        //2. If not, perform generation cycle on it and create necesery entities

                        //3. Then load entities list from database

                }
                
                
                
                @Override
                public synchronized void chunk_gc(){
                    //check if this chunk was not used for a long amt of time,
                    //far from any player,  or we running low on memory
                }
                
                public void unloadChunk(){
                    //save chunk data (region standings or smth. like this)
                    
                    //1. save chunk entities
                    
                    
                    
                    //2. unload them
                    //3. unload the chunk
                }
                
            };
            layer.set_zindex(i);
            worldLayers.put(i, layer);
        }
    }
}

//TODO: to be investigated - ehcache, Berkeley DB, DirectMemory/Redis