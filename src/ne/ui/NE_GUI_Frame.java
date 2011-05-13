/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import render.Render;
import render.Tileset;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Frame extends NE_GUI_Element{

    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;

    Tileset gui_tile;

    public NE_GUI_Frame(){
        r = (float)Math.random();
        g = (float)Math.random();
        b = (float)Math.random();

        gui_tile = new Tileset();
        //gui_tile.texture_name = "../ui/window_ui_small.png";
        gui_tile.TILESET_W = 4;
        gui_tile.TILESET_H = 4;
    }

    int t_window_w = 0;
    int t_window_h = 0;
    int WIN_TILE_SIZE = 32;

    /*
     *  Set window size in tiles
     *  recalculate w/h size
     */
    
    public void set_tw(int tw){
        t_window_w = tw;
        w = WIN_TILE_SIZE*t_window_w;

    }
    public void set_th(int th){
        t_window_h = th;
        h = WIN_TILE_SIZE*t_window_h;
    }

    @Override
    public void render(){

        /*glColor3f(r,g,b);
        glBegin(GL_QUADS);
            glVertex2f( x,   y);
            glVertex2f( x+w, y);
            glVertex2f( x+w, y+h);
            glVertex2f( x,   y+h);
        glEnd();*/

        
        Render.bind_texture("../ui/window_ui_small.png");

        int tile_id = 0;

        for(int i = 0; i<t_window_w; i++)
            for(int j = 0; j<t_window_h; j++){

                //tile_id = 6;    //center

                if (j == 0){
                    if (i==0)
                        tile_id = 0;    //ul
                    else if(i == t_window_w-1)
                        tile_id = 2;    //ur
                    else
                        tile_id = 1;    //uc
                }else if(j == t_window_h-1){
                    
                     if (i==0)
                        tile_id = 8;    //bl
                    else if(i == t_window_w-1)
                        tile_id = 10;    //br
                    else
                        tile_id = 9;    //bc
                }else{
                     if (i==0)
                        tile_id = 4;    //ml
                    else if(i == t_window_w-1)
                        tile_id = 6;    //mr
                    else
                        tile_id = 5;    //mc
                }
                //-------------------

                render_window_tile(i,j,tile_id);
                
            }

        
        super.render();
    }

    void render_window_tile(int i, int j, int tile_id){

        float tx = gui_tile.get_texture_x(tile_id);
        float ty = gui_tile.get_texture_y(tile_id);
        float ts = gui_tile.get_texture_w();


        int x = i * WIN_TILE_SIZE + this.x;
        int y = j * WIN_TILE_SIZE + this.y;
        
        int w = WIN_TILE_SIZE;
        int h = WIN_TILE_SIZE;

        glBegin(GL_QUADS);
                glTexCoord2f(tx, ty);
            glVertex2f( x,   y);
                glTexCoord2f(tx+ts, ty);
            glVertex2f( x+w, y);
                glTexCoord2f(tx+ts, ty+ts);
            glVertex2f( x+w, y+h);
                glTexCoord2f(tx, ty+ts);
            glVertex2f( x,   y+h);
        glEnd();
    }

}
