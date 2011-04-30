/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import ne.Input;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import render.WindowRender;
import world.WorldModel;
import world.WorldView;

/**
 *
 * @author Administrator
 */
public class TileCoordOverlay {
    public static void render(){

        if (!Input.key_state_alt){
            return;
        }

        int x = Mouse.getX();
        int y = Mouse.getY();


        Point tile_coord = WorldView.getTileCoord(x,y);
        Point chunk_coord = WorldModel.get_chunk_coord(tile_coord);

        y = WindowRender.get_window_h() - y;

        OverlaySystem.ttf.drawString(x+20, y-10,
                "["+Integer.toString(tile_coord.getX())+
                ","+Integer.toString(tile_coord.getY())+
                "] - @ ["
                   +Integer.toString(chunk_coord.getX())+
                ","+Integer.toString(chunk_coord.getY())+
                "]"

        , Color.white);
    }
}
