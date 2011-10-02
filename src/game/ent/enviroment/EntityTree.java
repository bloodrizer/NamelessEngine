/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.enviroment;

import actions.IAction;
import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import game.ent.Entity;
import items.BaseItem;
import java.util.ArrayList;
import player.Player;
import render.DebugRenderer;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntityTree extends Entity {

    TreeType treeType = TreeType.TREE_FIR;

    public enum TreeType {

        TREE_FIR("trees/fir.png");


        String textureName;
        TreeType(String textureName){
            this.textureName = textureName;
        }

        public String getTexture(){
            return textureName;
        }
    }

    @Override
     public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();


        __render.set_texture(treeType.getTexture());


        __render.get_tileset().sprite_w = 256;
        __render.get_tileset().sprite_h = 256;
        
        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;


        return __render;
    }
    //--------------------------------------------------------------------------


    @Override
    public ArrayList get_action_list(){

        class ActionCutTree extends BaseEntityAction{

            @Override
            public void execute() {
                if (assert_range()){
                    BaseItem item = BaseItem.produce("wood_block", 1);
                    Player.get_ent().container.add_item(item);
                }
            }

        }
        class ActionPickBranch extends BaseEntityAction{

            @Override
            public void execute() {
                if (assert_range()){
                    BaseItem item = BaseItem.produce("branch", 1);
                    Player.get_ent().container.add_item(item);
                }
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);

        if(Player.get_ent().container.has_item(
                BaseItem.produce("stone_axe", 1)
                )){
                    list.add_action(new ActionCutTree(),"Cut a tree");
                }

        list.add_action(new ActionPickBranch(),"Pick brunch");

        return list.get_action_list();
    }
}
