/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ai;

import game.ent.Entity;
import world.util.Fov;

/**
 *
 * @author Administrator
 */
public class AI {
    
    protected Entity owner;
    public void set_owner(Entity owner){
        this.owner = owner;
    }

    public void update(){
        
    }

    public void think(){
        
    }

    public boolean entity_in_fov(Entity ent){
        //todo: implement combat.get_fov();
        if (Fov.in_range(owner.origin, ent.origin, 5)){
            return true;
        }

        return false;
    }
}
