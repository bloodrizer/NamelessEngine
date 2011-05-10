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
        NPCRenderer render = new NPCRenderer();
        render.set_texture("player.png");

        render.set_animation_length(8);


        render.get_tileset().sprite_w = 32;
        render.get_tileset().sprite_h = 46;

        return render;
    }

}
