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
        BaseItem branch = BaseItem.produce("branch", 1);
        this.container.add_item(branch);
        System.out.println(branch.get_container());
    }

    @Override
     public boolean isPlayerEnt(){
        return true;
    }

     @Override
     public EntityRenderer build_render(){
        NPCRenderer __render = new NPCRenderer();
        __render.set_texture("player_hd.png");

        __render.set_animation_length(7);


        /*__render.get_tileset().sprite_w = 64;
        __render.get_tileset().sprite_h = 109;*/
        __render.get_tileset().sprite_w = 46;
        __render.get_tileset().sprite_h = 78;

        return __render;
    }

}
