/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import render.EntityRenderer;
import render.NPCRenderer;

/**
 *
 * @author Administrator
 */
public class EntityPlayer extends Entity{

    @Override
     public boolean isPlayerEnt(){
        return true;
    }

     @Override
     public EntityRenderer build_render(){
        NPCRenderer __render = new NPCRenderer();
        __render.set_texture("player.png");

        __render.set_animation_length(8);


        __render.get_tileset().sprite_w = 28;
        __render.get_tileset().sprite_h = 46;

        return __render;
    }

}
