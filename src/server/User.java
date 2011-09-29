/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import game.ent.Entity;

/**
 *
 * @author bloodrizer
 */
public class User {

    Entity playerEnt;

    public int getId() {
        return 123456789;
    }

    public void setEntity(Entity ent){
        this.playerEnt = ent;
    }

    public Entity getEntity(){
        return playerEnt;
    }
}
