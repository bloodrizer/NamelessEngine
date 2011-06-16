/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.monsters;

import game.ent.EntityNPC;
import render.EntityRenderer;
import render.NPCRenderer;

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
}
