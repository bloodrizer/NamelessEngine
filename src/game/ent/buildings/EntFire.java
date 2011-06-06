/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

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
}
