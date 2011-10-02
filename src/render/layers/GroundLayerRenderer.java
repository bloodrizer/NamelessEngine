/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.layers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import render.TilesetRenderer;
import world.WorldTile;
import world.WorldTile.TerrainType;
import world.WorldTimer;
import world.WorldView;

/**
 *
 * @author bloodrizer
 */
public class GroundLayerRenderer extends LayerRenderer{
    
    public static TilesetRenderer bg_tileset_renderer;

    TilesetRenderer tileSprite;

    public GroundLayerRenderer(){
        bg_tileset_renderer = new TilesetRenderer();
        bg_tileset_renderer.texture_name = "terrain_sprites.png";
        
        tileSprite = new TilesetRenderer();
        tileSprite.texture_name = "/render/terrain/grassland.png";
        tileSprite.sprite_w = 64;
        tileSprite.sprite_h = 68;
        tileSprite.TILESET_W = 1;
        tileSprite.TILESET_H = 1;
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
    public static Vector3f get_tile_color(WorldTile tile){


        float grass_r = (1.0f - (tile.moisture / 5.0f)) * 0.5f ;

        float lightLevelMtp = ((float)WorldView.getYOffset(tile)/96.0f)/5.0f;

        utl_tile_color.set(
                0.3f + grass_r + tile.light_level - lightLevelMtp   + WorldTimer.get_light_amt(),
                0.5f - grass_r/2.0f  + tile.light_level - lightLevelMtp   + WorldTimer.get_light_amt(),
                0.5f  + tile.light_level - lightLevelMtp     + WorldTimer.get_light_amt()
        );

        if (utl_tile_color.getY() > 0.6f){
            utl_tile_color.setY(0.6f);
        }

        return utl_tile_color;
    }
    
    private void render_bg_tile(int i, int j, WorldTile tile) {
        //throw new UnsupportedOperationException("Not yet implemented");
        //bg_tileset_renderer.render_bg_tile(i, j, tile.get_tile_id());


        //generates 0.0f - 1.0f height value

        if (i == 0 || j == 0){
            tileSprite.texture_name = "/render/terrain/grid_debug.png";
        }else{
            tileSprite.texture_name = "/render/terrain/grassland.png";
        }

        if (tile.is_blocked()){
            GL11.glColor3f(0.1f,0.1f,0.1f);
        }

        tileSprite.render_sprite(i, j, 1, 0, WorldView.getYOffset(tile)+32);    //replace 32 with actual magic constant based on tile sprite height



        //tileSprite.render_sprite(i, j, 1, 0, 32);
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
