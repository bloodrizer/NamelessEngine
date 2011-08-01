/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import game.combat.BasicCombat;
import game.combat.Combat;
import items.BaseItem;
import render.EntityRenderer;
import render.NPCRenderer;
import world.WorldModel;

/**
 *  Entity, representing player on a game map
 *
 * Note that due to the buggy player ent checking, this class
 * must be used *ONLY!!!!* for main player instancing
 *
 */
public class EntityPlayer extends EntityNPC {

    public EntityPlayer(){

        Combat __combat = new BasicCombat();
        __combat.set_hp(500);
        

        set_blocking(true);
        set_combat(__combat);

        //inventory shit - debug only
        //BaseItem branch = BaseItem.produce("branch", 20);
        this.container.add_item(
                BaseItem.produce("branch", 20)
        );
        this.container.add_item(
                BaseItem.produce("stone", 20)
        );
        this.container.add_item(
                BaseItem.produce("stone_axe", 1)
        );
        this.container.add_item(
                BaseItem.produce("wood_block", 64)
        );
        this.container.add_item(
                BaseItem.produce("wood_wall", 64)
        );
        this.container.add_item(
                BaseItem.produce("chest", 16)
        );
        this.container.add_item(
                BaseItem.produce("fire", 2)
        );
        this.container.add_item(
                BaseItem.produce("signpost", 64)
        );
        this.container.add_item(
                BaseItem.produce("copper_coin", 10)
        );
        //System.out.println(branch.get_container());


        //----------- equip test -----------
        this.container.add_item(
                BaseItem.produce("valkyrie_helmet", 1).set_slot("head")
        );
    }

    @Override
     public boolean isPlayerEnt(){
        return true;
    }

     @Override
     public EntityRenderer build_render(){
        /*NPCRenderer __render = new NPCRenderer();
        __render.set_texture("player_hd.png");

        __render.set_animation_length(7);*/

        NPCRenderer __render = (NPCRenderer)(super.build_render());

        

        return __render;
    }

    /*@Override
     public void update(){
         super.update();

     }*/

    @Override
    public void e_on_change_item(){
        if (get_active_item() != null && get_active_item().get_type().equals("torch")){
             light_amt = 4.0f;
             WorldModel.invalidate_light();
        }else{
             if (light_amt != 0.0f){
                light_amt = 0.0f;
                WorldModel.invalidate_light();
            }
        }

        
    }

}
