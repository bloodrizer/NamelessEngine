/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import render.TilesetRender;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author Administrator
 */
public class WorldView {

    public TilesetRender bg_tileset = new TilesetRender();

    public void synchronize(WorldModel model){
        
    }

    public static int TILEMAP_W = 30;
    public static int TILEMAP_H = 30;

    public void render_background(){

        for (int i = 0; i<TILEMAP_W; i++){
            for (int j = 0; j<TILEMAP_H; j++){
               bg_tileset.render_tile(i,j, 0);
            }
        }
    }

    public void render(){
        render_background();
    }
}
