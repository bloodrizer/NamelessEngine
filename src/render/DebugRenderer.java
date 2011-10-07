/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import render.overlay.OverlaySystem;
import world.WorldView;

/**
 *
 * @author Administrator
 */
public class DebugRenderer extends EntityRenderer  {

    private TilesetRenderer tileset = null;

    public DebugRenderer(){

        tileset = new TilesetRenderer();
        tileset.texture_name = "tileset1.png";
    }

    @Override
    public void render(){

        if(ent == null){
            return;
        }

        this.tileset.render_sprite(
            ent.origin.getX(),
            ent.origin.getY(),
            8,   //hardcoded, lol,
            0,
            WorldView.getYOffset(ent.tile)

        );

        int ent_screen_x = WorldView.world2local_x(
                ent.origin.getX()*tileset.TILE_SIZE,
                ent.origin.getY()*tileset.TILE_SIZE
        );
        int ent_screen_y = WorldView.world2local_y(
                ent.origin.getX()*tileset.TILE_SIZE,
                ent.origin.getY()*tileset.TILE_SIZE
        );
        OverlaySystem.ttf.drawString(ent_screen_x, ent_screen_y+5,
                ent.getClass().getName().
                concat(":").
                concat(ent.toString()),
        Color.white);
    }
}
