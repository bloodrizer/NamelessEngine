/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import client.ClientWorld;
import game.ent.Entity;
import player.Player;
import render.EntityRenderer;
import render.SpriteRenderer;
import world.layers.WorldLayer;
import world.WorldModel;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public class EntFence extends EntBuilding {
    @Override
    public EntityRenderer build_render(){
        final EntFence __owner = this;
        SpriteRenderer __render = new SpriteRenderer(){
            EntFence ent;
            {
                this.ent = __owner;
            }
            
            @Override
            public void render(){
                tile_id = get_tile_id();
                super.render();
            }

            public int get_tile_id(){

                int id = 0;

                boolean t = getLayer().get_tile(ent.x(), ent.y()-1).has_ent(EntFence.class);
                boolean b = getLayer().get_tile(ent.x(), ent.y()+1).has_ent(EntFence.class);
                boolean l = getLayer().get_tile(ent.x()-1, ent.y()).has_ent(EntFence.class);
                boolean r = getLayer().get_tile(ent.x()+1, ent.y()).has_ent(EntFence.class);

                if (t){
                    id = 1;
                    if (r){
                        if(!l && !b){
                            id = 4;
                        }else{
                            id = 2;
                        }
                    }
                    if(l)
                    {
                        if(!r && !b){
                            id = 5;
                        }else{
                            id = 2;
                        }
                    }
                }
                if (b){
                    id = 1;
                    if (r){
                        if(!l && !t){
                            id = 3;
                        }else{
                            id = 2;
                        }
                    }
                    if(l)
                    {
                        if(!r && !t){
                            id = 6;
                        }else{
                            id = 2;
                        }
                    }
                }

                /*if (t.has_ent(EntFence.class) || b.has_ent(EntFence.class)){
                    id += 1;
                    if (l.has_ent(EntFence.class) || r.has_ent(EntFence.class)){
                        id += 1;
                    }
                }*/
               



                return id;
            }

            private WorldLayer getLayer() {
                return ClientWorld.getWorldLayer(Player.get_zindex());
            }
        };
        __render.set_texture("/render/gfx/ents/fence.png");

        __render.get_tileset().sprite_w = 60;
        __render.get_tileset().sprite_h = 60;

        __render.get_tileset().TILESET_W = 3;
        __render.get_tileset().TILESET_H = 3;

        //__render.set_tile_id((int)(Math.random()*8));

        return __render;
    }
}
