/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.enviroment;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import game.ent.Entity;
import items.BaseItem;
import java.util.ArrayList;
import player.Player;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntityStone extends Entity {
    @Override
    public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("stone_big.png");

        __render.get_tileset().sprite_w = 64;
        __render.get_tileset().sprite_h = 64;

        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;

        return __render;
    }

    @Override
    public ArrayList get_action_list(){

        class ActionChipStone extends BaseEntityAction{

            @Override
            public void execute() {
                if (Player.in_range(owner)){
                    BaseItem stone = BaseItem.produce("stone", 1);
                    Player.get_ent().container.add_item(stone);
                }else{
                    Player.move(owner.origin);
                }
                //TODO: else - move close to entity
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionChipStone(),"Chip stone");

        return list.get_action_list();
    }
    
}
