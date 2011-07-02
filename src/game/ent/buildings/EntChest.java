/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import java.util.ArrayList;
import ne.Game;
import ne.Main;
import ne.ui.NE_GUI_Element;
import ne.ui.NE_GUI_Inventory;
import render.EntityRenderer;
import render.SpriteRenderer;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class EntChest extends EntBuilding{
    @Override
    public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/gfx/ents/chest.png");

        __render.get_tileset().sprite_w = 60;
        __render.get_tileset().sprite_h = 60;

        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;

        return __render;
    }

    @Override
    public ArrayList get_action_list(){
        class ActionOpenChest extends BaseEntityAction{

            @Override
            public void execute() {
                NE_GUI_Element root = Game.get_ui_root();

                NE_GUI_Inventory chest_inv = new NE_GUI_Inventory();
                root.add_at(0,chest_inv);
                chest_inv.set_container(owner.container);
                chest_inv.set_title("Chest");

                chest_inv.x = WindowRender.get_window_w() / 2;  //user right side of the screen
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionOpenChest(),"Open chest");

        return list.get_action_list();
    }
}
