/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import items.BaseItem;
import render.EntityRenderer;
import render.NPCRenderer;

/**
 *  Entity, representing player on a game map
 *
 * Note that due to the buggy player ent checking, this class
 * must be used *ONLY!!!!* for main player instancing
 *  TODO: make EntityPlayer extanding NPC_Entity
 *
 */
public class EntityPlayer extends EntityNPC {

    public EntityPlayer(){
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
                BaseItem.produce("fire", 1)
        );
        this.container.add_item(
                BaseItem.produce("fence", 64)
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

}
