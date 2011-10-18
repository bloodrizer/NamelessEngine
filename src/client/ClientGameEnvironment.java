/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import game.GameEnvironment;
import game.ent.EntityManager;
import world.WorldModel;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class ClientGameEnvironment {
    static GameEnvironment env = null;

    public static GameEnvironment getEnvironment(){
        if (env == null){
            env = new GameEnvironment(){
                
            };
        }
        return env;
    }

    public static void setEnvironment(GameEnvironment clientGameEnvironment) {
        env = clientGameEnvironment;
    }

    public static WorldModel getWorldModel(){
        return env.getWorld();
    }

    public static WorldLayer getWorldLayer(int layerID){
        return getWorldModel().getWorldLayer(layerID);
    }

    public static EntityManager getEntityManager() {
        return env.getEntityManager();
    }

}
