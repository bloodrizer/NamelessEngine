/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.monsters;

import game.combat.BasicCombat;
import game.ent.EntityNPC;
import game.ent.controller.NpcController;
import render.EntityRenderer;
import render.NPCRenderer;
import world.WorldTimer;

/**
 *
 * @author Administrator
 */
public class Zombie extends EntityNPC {
     @Override
     public EntityRenderer build_render(){
        NPCRenderer __render = (NPCRenderer)(super.build_render());
        __render.set_texture("zombie.png");


        return __render;
    }

    public Zombie(){
        set_controller(new NpcController());
        set_combat(new BasicCombat());
    }

    @Override
     public void think(){
         super.think();

         sleep(500);

         if(!WorldTimer.is_night()){
             combat.take_damage(new Damage(1,DamageType.DMG_FIRE));
         }
     }
}
