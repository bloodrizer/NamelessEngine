/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.layers;

import org.lwjgl.opengl.GL11;
import render.TilesetRenderer;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public class UndergroundLayerRenderer extends LayerRenderer{
    public static TilesetRenderer bg_tileset_renderer;
    public UndergroundLayerRenderer(){
        bg_tileset_renderer = new TilesetRenderer();
    }

    @Override
    public void render_tile(WorldTile tile, int tile_x, int tile_y) {
        if (tile != null){

            GL11.glColor3f(1.0f,1.0f,1.0f);
            render_bg_tile(tile_x, tile_y, tile);
       }
    }


    private void render_bg_tile(int i, int j, WorldTile tile) {
        //throw new UnsupportedOperationException("Not yet implemented");
        bg_tileset_renderer.render_bg_tile(i, j, tile.get_tile_id());

    }
}
