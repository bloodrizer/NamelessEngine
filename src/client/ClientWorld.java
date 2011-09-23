/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import world.WorldModel;
import world.layers.WorldLayer;

/**
 *
 * @author bloodrizer
 */
public class ClientWorld {
    static WorldModel clientWorld = null;
    
    public static WorldModel getWorld(){
        if (clientWorld == null){
            clientWorld = new WorldModel();
        }
        return clientWorld;
    }
    
    public static WorldLayer getWorldLayer(int layerId){
        return getWorld().getWorldLayer(layerId);
    }
}
