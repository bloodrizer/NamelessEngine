/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.combat;

import game.ent.Entity;

/**
 *  This is generic combat engine, that is only inflicting damage based on hp
 */
public class BasicCombat extends Combat{
    @Override
    public void take_damage(Damage damage){

    }

    public int get_damage_amt(){
        return 5;
    }

    @Override
    public void inflict_damage(Entity ent){
        Combat ent_combat = ent.get_combat();
        if (ent_combat != null){
            //do something there
            ent_combat.take_damage(new Damage(get_damage_amt()));
        }
    }
        
}
