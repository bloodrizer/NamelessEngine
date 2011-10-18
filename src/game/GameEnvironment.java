/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import events.EventManager;
import game.ent.EntityManager;
import world.WorldModel;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */

/*
 * Container that binds former static EventManager, WorldModel and EntityManager into one entity
 *
 * Used to allow client and server on the same application to have own model of game processes
 *
 */

public abstract class GameEnvironment {

    protected EntityManager entManager = null;
    protected EventManager manager = null;
    protected WorldModel clientWorld = null;


    public EventManager getEventManager(){
        throw new RuntimeException("requesting EventManager on abstract GameEnvironment");
    }

    public EntityManager getEntityManager(){
        if (entManager == null){
            
            entManager = new EntityManager();
            entManager.setEnviroment(this);
        }
        return entManager;
    }

    public WorldModel getWorld(){
        if (clientWorld == null){
            
            clientWorld = new WorldModel();
            clientWorld.setEnvironment(this);
        }
        return clientWorld;
    }

    public WorldLayer getWorldLayer(int layerId){
        return getWorld().getWorldLayer(layerId);
    }
}
