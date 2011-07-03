/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import java.util.ArrayList;
import ne.Game;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntSignpost extends EntBuilding{

    public static String text = "Signpost text";

    @Override
    public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/gfx/ents/signpost.png");

        __render.get_tileset().sprite_w = 40;
        __render.get_tileset().sprite_h = 40;

        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;

        return __render;
    }

     @Override
    public ArrayList get_action_list(){
        class ActionRead extends BaseEntityAction{

            @Override
            public void execute() {
                Game.get_ui().show_message("Signpost",text);
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionRead(),"Read");
        return list.get_action_list();
    }
}
