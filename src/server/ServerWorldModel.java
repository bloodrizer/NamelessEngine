/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

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

                    //1. Load chunk from db and check if we did not generate it allready

                    //2. If not, perform generation cycle on it and create necesery entities

                    //2b.If yes, perform terrain-only generation

                    //3. Then load entities list from database
                }
            };
            layer.set_zindex(i);
            worldLayers.put(i, layer);
        }
    }
}
