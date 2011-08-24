/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.layers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import render.TilesetRenderer;
import world.WorldTile;
import world.WorldTile.TerrainType;
import world.WorldTimer;

/**
 *
 * @author bloodrizer
 */
public class GroundLayerRenderer extends LayerRenderer{
    
    public static TilesetRenderer bg_tileset_renderer;
    public GroundLayerRenderer(){
        bg_tileset_renderer = new TilesetRenderer();
    }

    @Override
    public void render_tile(WorldTile tile, int tile_x, int tile_y) {
        if (tile != null){

            //lil hack for terrain rendering visualization
            if (tile.terrain_type != TerrainType.TERRAIN_WATER){
                Vector3f tile_color = get_tile_color(tile);
                GL11.glColor3f(tile_color.x,tile_color.y,tile_color.z);
            }else{
                GL11.glColor3f(1.0f,1.0f,1.0f);
            }

            render_bg_tile(tile_x, tile_y, tile);
       }
    }
    
    static Vector3f utl_tile_color = new Vector3f();
    public Vector3f get_tile_color(WorldTile tile){
        //float g_color = ((float)tile.get_height() / 255);
        float g_color = 0.5f;
        utl_tile_color.set(
                0.5f + tile.light_level     + WorldTimer.get_light_amt(),
                g_color+ tile.light_level   + WorldTimer.get_light_amt(),
                0.5f+ tile.light_level      + WorldTimer.get_light_amt()
        );

        return utl_tile_color;
    }
    
    private void render_bg_tile(int i, int j, WorldTile tile) {
        //throw new UnsupportedOperationException("Not yet implemented");
        bg_tileset_renderer.render_bg_tile(i, j, tile.get_tile_id());

        /*
         * So far, tileset id acts like texture z-index.
         * Higher texture is allowed to wrap over lower texture, using alpha blending mask
         */
        /*Point _point = new Point(0,0);

        for (int _i = i-1; _i< i+1; _i++){
            for(int _j = j-1; _j < j+1; j++){
                if (WorldCluster.tile_in_cluster(_i,_j)){

                    _point.setLocation(_i, _j);
                    TextureTransition trans = bg_transition_map.get(_point);
                    if (trans == null){
                        trans = new TextureTransition();
                        bg_transition_map.put(_point, trans);
                    }
                    

                }
            }
        }*/
    }
    
}
