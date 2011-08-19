/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.decals;

import game.ent.EntityDecal;
import render.EntityRenderer;
import render.SpriteRenderer;

/**
 *
 * @author Administrator
 */
public class EntDecalBlood extends EntityDecal{
    @Override
    public EntityRenderer build_render(){

        SpriteRenderer __render = new SpriteRenderer();
        __render.set_texture("/render/gfx/decals/blood.png");

        __render.get_tileset().sprite_w = 32;
        __render.get_tileset().sprite_h = 32;

        __render.get_tileset().TILESET_W = 1;
        __render.get_tileset().TILESET_H = 1;

        __render.set_tile_id(0);

        return __render;

    }
}
