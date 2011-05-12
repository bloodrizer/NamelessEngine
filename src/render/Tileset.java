/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import java.io.FileInputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Point;
import static org.lwjgl.util.glu.GLU.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import world.WorldChunk;
import world.WorldTile;
import world.WorldView;

/**
 *
 * @author Administrator
 */
public class Tileset{
    //world grid size - critical for offset calculation
    public static int TILE_SIZE = 32;

    //TODO: set to non-static
    public int TILESET_SIZE = 8;
    public int TILESET_W = 8;
    public int TILESET_H = 8;

    public int sprite_w = 32;
    public int sprite_h = 32;

    public String texture_name = "tileset1.png";

    public Tileset(){
    }

    float get_texture_w(){
        return 1.0f / TILESET_W;
    }
    float get_texture_h(){
        return 1.0f / TILESET_H;
    }
    float get_texture_x(int tile_id){
        return 1.0f / TILESET_W * (tile_id);
    }

    float get_texture_y(int tile_id){
        return 1.0f / TILESET_H * ((tile_id) / TILESET_W );
    }

    private float dx = 0.0f;
    private float dy = 0.0f;

    //holy crap this shit is smely
    public void set_offset(float dx, float dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void render_tile(int i, int j, int tile_id){    
        Render.bind_texture(texture_name);

        if (i % WorldChunk.CHUNK_SIZE == 0){
            tile_id = 8;
        }
        if (j % WorldChunk.CHUNK_SIZE == 0){
            tile_id = 8;
        }


        draw_quad(
                i*TILE_SIZE,
                j*TILE_SIZE,
                sprite_w-1,
                sprite_h-1,
                tile_id
        );
    }

    private void draw_quad(int x, int y, int w, int h, int tile_id){

        float tx = get_texture_x(tile_id);
        float ty = get_texture_y(tile_id);
        float ts_w = get_texture_w();
        float ts_h = get_texture_h();

        glBegin(GL_QUADS);
            glTexCoord2f(tx, ty);
        glVertex2f( x,   y);
            glTexCoord2f(tx+ts_w, ty);
	glVertex2f( x+w, y);
            glTexCoord2f(tx+ts_w, ty+ts_h);
	glVertex2f( x+w, y+h);
            glTexCoord2f(tx, ty+ts_h);
	glVertex2f( x,   y+h);

        glEnd();
    }

    public void render_sprite(int i, int j, int tile_id){


        Render.bind_texture(texture_name);

        int x_world = i*TILE_SIZE + (int)(dx*TILE_SIZE);
        int y_world = j*TILE_SIZE + (int)(dy*TILE_SIZE);


        int x_local = WorldView.world2local_x(x_world,y_world);
        int y_local = WorldView.world2local_y(x_world,y_world);

        //quad_coord = WorldView.world2local(quad_coord);

        int w = TILE_SIZE-1;
        int h = TILE_SIZE-1;

        /*
         *  sprite offset ---->  *-----------------*
         *                       |                 |
         *          tile offset-----> * ----- *    |
         *                       |    |       |    |
         *                       |    |       |    |
         *                       |    |       |    |
         *                       *----*---X---*----*
         *              +TILE_SIZE/2 -----^
         */

         //TODO:recalculate sprite

        int local_sprite_w = (int)((float)sprite_w * WorldView.ISOMETRY_TILE_SCALE / WorldView.TILE_UPSCALE);
        int local_sprite_h = (int)((float)sprite_h * WorldView.ISOMETRY_TILE_SCALE / WorldView.TILE_UPSCALE);

        draw_quad(
                x_local - local_sprite_w/2,
                y_local - local_sprite_h + (int)(TILE_SIZE * WorldView.ISOMETRY_TILE_SCALE / WorldView.TILE_UPSCALE),
                local_sprite_w,
                local_sprite_h,
                tile_id
        );

    }

    //wrapper for isometric background render
    public void render_bg_tile(int i, int j, int tile_id){
        
        float scale = WorldView.ISOMETRY_TILE_SCALE;
        float angle = WorldView.ISOMETRY_Y_SCALE;

        if (WorldView.ISOMETRY_MODE){

            glPushMatrix();
            //glScalef( 2.0f, 1.2f, 1.0f);
            glScalef( 1.0f*scale, angle*scale, 1.0f);
            glRotatef( WorldView.ISOMETRY_ANGLE, 0.0f, 0.f, 1.0f );

            render_tile(i,j, tile_id);

            glPopMatrix();

        }else{
            render_tile(i,j, tile_id);
        }

    }
}
