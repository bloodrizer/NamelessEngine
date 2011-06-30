/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.decals;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import game.ent.EntityDecal;
import items.BaseItem;
import java.util.ArrayList;
import player.Player;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntDecalFlower extends EntityDecal{
    @Override
    public EntityRenderer build_render(){

        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/terrain_sprites.png");

        __render.get_tileset().sprite_w = 32;
        __render.get_tileset().sprite_h = 32;

        __render.get_tileset().TILESET_W = 8;
        __render.get_tileset().TILESET_H = 8;

        __render.set_tile_id(23);

        return __render;

    }

    @Override
    public ArrayList get_action_list(){

        class ActionPickFlower extends BaseEntityAction{

            @Override
            public void execute() {
                if (assert_range()){
                    BaseItem item = BaseItem.produce("flower", 1);
                    Player.get_ent().container.add_item(item);

                    owner.trash();
                }
            }

        }
        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionPickFlower(),"Pick flower");

        return list.get_action_list();
    }
}
