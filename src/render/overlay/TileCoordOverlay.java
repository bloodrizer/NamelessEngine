/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import client.ClientGameEnvironment;
import game.ent.Entity;
import ne.Input;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import player.Player;
import render.WindowRender;
import world.WorldChunk;
import world.layers.WorldLayer;
import world.WorldModel;
import world.WorldTile;
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
        Point chunk_coord = WorldChunk.get_chunk_coord(tile_coord);

        y = WindowRender.get_window_h() - y;

        OverlaySystem.ttf.drawString(x+20, y-10,
                "Mouse: ["+Integer.toString(x)+
                ","+Integer.toString(y)+
                "] - World: ["+
                    Integer.toString(tile_coord.getX())+
                ","+Integer.toString(tile_coord.getY())+
                "] - Chunk@: ["
                   +Integer.toString(chunk_coord.getX())+
                ","+Integer.toString(chunk_coord.getY())+
                "]"

        , Color.white);


        WorldTile tile = ClientGameEnvironment.getWorldLayer(Player.get_zindex()).get_tile(tile_coord.getX(), tile_coord.getY());

        if (tile != null){
            Object[] ent_list = tile.ent_list.toArray();
            OverlaySystem.ttf.drawString(x+20, y+10,
                    /*"entities:" + Integer.toString(ent_list.length) +
                    "blocked:" + Boolean.toString(tile.is_blocked()) +*/
                    "light level:" + tile.light_level +
                    " height:" + tile.get_height() + "("+tile.get_elevation_zone()+")" +
                    " moist:" + tile.moisture + "("+tile.get_moisture_zone()+")" +
                    " biome:" + tile.biome_type
            );
        }

    }
}
