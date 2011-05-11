/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import org.newdawn.slick.Color;
import render.overlay.OverlaySystem;
import world.WorldView;

/*
 * Composite npc renderer
 *
 *
 * It features basic animation and [nwse] sprite orientation
 *
 *
 */


public class NPCRenderer extends EntityRenderer{

    private Tileset tileset = null;

    public int ANIMATION_LENGTH = 0;
    private int frame_id = 0;

    public void next_frame(){
        frame_id++;
        if(frame_id>=(ANIMATION_LENGTH-1)){     //4-frame animation, hardcoded, lol
            frame_id = 0;
        }
    }

    public NPCRenderer(){
        tileset = new Tileset();
    }

    public Tileset get_tileset(){
        return tileset;
    }

    public void set_texture(String name){
        tileset.texture_name = name;
    }

    public void set_animation_length(int frames){
        this.ANIMATION_LENGTH = frames;

         get_tileset().TILESET_W = ANIMATION_LENGTH;
         get_tileset().TILESET_H = 4;
    }

    private int get_tile_id(){
        int tile_id = 0;
        switch(ent.orientation){
            case ORIENT_N:
                tile_id = 0;
            break;
            case ORIENT_E:
                tile_id = 8;
            break;
            case ORIENT_S:
                tile_id = 16;
            break;
            case ORIENT_W:
                tile_id = 24;
            break;
        }

        tile_id = tile_id+frame_id;
        return tile_id;
    }

    @Override
    public void render(){
        tileset.set_offset(ent.dx, ent.dy);

        tileset.render_sprite(
            ent.origin.getX(),
            ent.origin.getY(),
            get_tile_id()
        );


        /*int ent_screen_x = WorldView.world2local_x(
                ent.origin.getX()*tileset.TILE_SIZE,
                ent.origin.getY()*tileset.TILE_SIZE
        );
        int ent_screen_y = WorldView.world2local_y(
                ent.origin.getX()*tileset.TILE_SIZE,
                ent.origin.getY()*tileset.TILE_SIZE
        );
        OverlaySystem.ttf.drawString(ent_screen_x, ent_screen_y+5,
                "dx:"+Float.toString(ent.dx) +
                ","+
                "dy:"+Float.toString(ent.dy),
        Color.white);*/
    }
}
