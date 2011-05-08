/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

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
        SpriteRenderer render = new SpriteRenderer();
        render.set_texture("tree.png");

        if (Math.random() > 0.5f){
            render.set_tile_id(1);
        }else{
            render.set_tile_id(2);
        }

        render.get_tileset().sprite_w = 64;
        render.get_tileset().sprite_h = 128;
        
        render.get_tileset().TILESET_W = 2;
        render.get_tileset().TILESET_H = 1;


        /*render.get_tileset().TILESET_SIZE = 196;
        render.get_tileset().TILE_SIZE = 64;*/


        return render;
    }
}
