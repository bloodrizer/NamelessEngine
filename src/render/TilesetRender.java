/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import java.io.FileInputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Administrator
 */
public class TilesetRender {
    public static int TILE_SIZE = 32;
    public static int TILESET_SIZE = 8;

    public String texture_name = "tileset1.png";

    public TilesetRender(){
    }

    static float get_texture_size(){
        return 1.0f / TILESET_SIZE;
    }
    static float get_texture_x(int tile_id){
        return 1.0f / TILESET_SIZE * (tile_id);
    }

    static float get_texture_y(int tile_id){
        return 1.0f / TILESET_SIZE * ((tile_id) / TILESET_SIZE );
    }

    public void render_tile(int i, int j, int tile_id){
        Render.bind_texture(texture_name);

        glBegin(GL_QUADS);
        
        //int tile_id = (int)(80*Math.random());
        //int tile_id = 0;

        float tx = get_texture_x(tile_id);
        float ty = get_texture_y(tile_id);
        float ts = get_texture_size();

        //glColor3f(1.0f,1.0f,1.0f);
            glTexCoord2f(tx, ty);
        glVertex2f( i*TILE_SIZE,         j*TILE_SIZE);
            glTexCoord2f(tx+ts, ty);
	glVertex2f((i+1)*TILE_SIZE-1 ,   j*TILE_SIZE);
            glTexCoord2f(tx+ts, ty+ts);
	glVertex2f((i+1)*TILE_SIZE-1,    ((j+1)*TILE_SIZE-1));
            glTexCoord2f(tx, ty+ts);
	glVertex2f(i*TILE_SIZE,          ((j+1)*TILE_SIZE-1));

        glEnd();

    }
}
