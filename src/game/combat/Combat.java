/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.combat;

import events.ETakeDamage;
import game.ent.Entity;

/**
 *
 * This is a simple combat wrapper
 */
public abstract class Combat {
    public Stats stats = new Stats();
    int hp = 0;
    Entity owner;

    public void set_owner(Entity owner){
        this.owner = owner;
    }

    public void take_damage(Damage damage){
    }

    public int get_hp(){
        return hp;
    }

    public boolean is_alive(){
        return hp>0;
    }

    /*
     * This method inflicts damage on given entity,
     * based on current combat mechanics of this entity
     *
     */
    public void inflict_damage(Entity ent){

    }

}
