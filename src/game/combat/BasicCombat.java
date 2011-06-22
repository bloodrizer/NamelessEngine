/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.combat;

import events.ETakeDamage;
import game.ent.Entity;
import game.ent.EntityActor;

/**
 *  This is generic combat engine, that is only inflicting damage based on hp
 */
public class BasicCombat extends Combat{


    @Override
    public void take_damage(Damage damage){
        
        ETakeDamage event = new ETakeDamage(this.owner, damage);
        event.post();

        hp = hp-damage.amt;

        if (!is_alive()){
            die(damage.inflictor);
        }
    }

    public void die(Entity killer){
        //TODO: post event there
        owner.trash();

        if (owner instanceof EntityActor){
            ((EntityActor)owner).die(killer);
        }
    }

    public int get_damage_amt(){
        return 2;
    }

    @Override
    public int get_max_hp(){
        return 10;
    }

    @Override
    public void inflict_damage(Entity ent){
        super.inflict_damage(ent);

        Combat ent_combat = ent.get_combat();
        if (ent_combat != null){
            //do something there
            Damage dmg = new Damage(get_damage_amt());
            dmg.set_inflictor(this.owner);
            ent_combat.take_damage(dmg);
        }
    }
        
}
