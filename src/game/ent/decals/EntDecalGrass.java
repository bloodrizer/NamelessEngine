/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.decals;

import game.ent.EntityDecal;
import render.EntityRenderer;
import render.SpriteRenderer;

import render.Tileset;

/**
 *
 * @author Administrator
 */
public class EntDecalGrass extends EntityDecal {

    @Override
    public EntityRenderer build_render(){

        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/terrain_sprites.png");

        __render.get_tileset().sprite_w = 32;
        __render.get_tileset().sprite_h = 32;

        __render.get_tileset().TILESET_W = 8;
        __render.get_tileset().TILESET_H = 8;

        if (Math.random() < 0.5f){
            __render.set_tile_id(20);
        }else{
            __render.set_tile_id(19);
        }

        return __render;

    }
}
