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

    private int bloodType = 0;
    private int bloodPhase = 0;

    /*
     * Sets next tile of the blood sprite, making it look bigger
     *
     */
    public void nextTile(){  
        if (bloodPhase < 3){
            bloodPhase++;
        }
    }

    public EntDecalBlood(){
        bloodType = (int)(Math.random()*4)+1;
        bloodPhase = 0;

        this.dx = (float)Math.random();
        this.dy = (float)Math.random();
    }

    @Override
    public EntityRenderer build_render(){

        SpriteRenderer __render = new SpriteRenderer(){
            @Override
            public void render(){
                tile_id = get_tile_id();
                super.render();
            }

            public int get_tile_id(){
                return bloodType*4 + bloodPhase;
            }
        };

        __render.set_texture("/render/gfx/decals/blood.png");

        __render.get_tileset().sprite_w = 32;
        __render.get_tileset().sprite_h = 32;

        __render.get_tileset().TILESET_W = 4;
        __render.get_tileset().TILESET_H = 4;

        __render.set_tile_id(bloodType*4 + bloodPhase);

        return __render;

    }
}
