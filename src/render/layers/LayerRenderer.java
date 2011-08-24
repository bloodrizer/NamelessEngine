/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.layers;

import world.WorldTile;

/**
 *
 * @author bloodrizer
 */
public abstract class LayerRenderer {
    public abstract void render_tile(WorldTile tile, int tile_x, int tile_y);
}
