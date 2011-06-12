/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import items.BaseItem;
import items.EquipContainer;
import items.ItemContainer;
import render.EntityRenderer;
import render.NPCRenderer;

/**
 *  This is generic class for every NPC,
 *  mostly players
 */

public class EntityNPC extends EntityActor {
    //name to show above
    public String name = "undefined";

    public EquipContainer equipment;

    public void EquipItem(BaseItem item){
        //if (BaseItem)
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
