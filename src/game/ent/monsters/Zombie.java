/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.monsters;

import game.combat.BasicCombat;
import game.combat.Damage;
import game.combat.Damage.DamageType;
import game.ent.Entity;
import game.ent.controller.MobController;
import game.ent.controller.NpcController;
import render.EntityRenderer;
import render.NPCRenderer;
import world.WorldTimer;

/**
 *
 * @author Administrator
 */
public class Zombie extends EntMonster {
     @Override
     public EntityRenderer build_render(){
        NPCRenderer __render = (NPCRenderer)(super.build_render());
        __render.set_texture("zombie.png");


        return __render;
    }

    public Zombie(){
        MobController __controller = new MobController();
        __controller.NEXT_FRAME_DELAY = 200;
        __controller.MOVE_SPEED = 0.02f;

        set_controller(__controller);
        set_combat(new BasicCombat());

        set_blocking(true);

        
    }

    @Override
     public void think(){
         super.think();
         
         if(!WorldTimer.is_night()){
             combat.take_damage(new Damage(1,DamageType.DMG_FIRE));
         }

         if ((int)(Math.random()*100) < 3){
             say_message("Braaains!");
         }
         
         sleep(500);
     }

    @Override
    public void die(Entity killer){
        drop_loot(killer,"bone",1,20);
    }
}
