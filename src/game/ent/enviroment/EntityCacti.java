/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.enviroment;

import game.ent.Entity;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntityCacti extends Entity{
     @Override
     public EntityRenderer build_render(){
        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/terrain_sprites.png");

        __render.set_tile_id(22);

        __render.get_tileset().sprite_w = 64;
        __render.get_tileset().sprite_h = 64;

        __render.get_tileset().TILESET_W = 8;
        __render.get_tileset().TILESET_H = 8;

        return __render;
    }
}
