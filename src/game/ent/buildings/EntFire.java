/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import items.BaseItem;
import java.util.ArrayList;
import player.Player;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 * Fire. Builded with 5 branches
 */
public class EntFire extends EntBuilding {

    public EntFire(){
        this.light_amt = 15.0f;
    }

    @Override
    public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/gfx/ents/fire.png");

        __render.get_tileset().sprite_w = 40;
        __render.get_tileset().sprite_h = 40;

        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;

        return __render;
    }

    @Override
    public ArrayList get_action_list(){
        class ActionLightTorch extends BaseEntityAction{

            @Override
            public void execute() {
                if (assert_range()){
                    Player.get_ent().container.add_item(BaseItem.produce("torch", 1));
                    Player.get_ent().container.remove_item("branch",3);
                }
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);

        if(Player.get_ent().container.has_item(
        BaseItem.produce("branch", 3)
        )){
            list.add_action(new ActionLightTorch(),"Light torch");
        }
        return list.get_action_list();
    }
}
