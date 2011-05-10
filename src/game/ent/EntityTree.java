/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import java.util.ArrayList;
import render.DebugRenderer;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntityTree extends Entity {

    @Override
     public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("tree.png");

        if (Math.random() > 0.5f){
            __render.set_tile_id(1);
        }else{
            __render.set_tile_id(2);
        }

        __render.get_tileset().sprite_w = 64;
        __render.get_tileset().sprite_h = 128;
        
        __render.get_tileset().TILESET_W = 2;
        __render.get_tileset().TILESET_H = 1;


        /*render.get_tileset().TILESET_SIZE = 196;
        render.get_tileset().TILE_SIZE = 64;*/


        return __render;
    }
    //--------------------------------------------------------------------------


    @Override
    public IEntityAction[] get_action_list(){

        class ActionCutTree extends BaseEntityAction{

            @Override
            public void execute() {
                System.out.println("ActionCutTree");
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionCutTree(),"Cut a tree");
        list.add_action(new ActionCutTree(),"Pick brunch");

        return list.get_action_list();
    }
}
