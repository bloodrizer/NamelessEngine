/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import org.lwjgl.opengl.GL11;

/**
 *
 * @Simple simple sprite renderer for inanimated objects like trees / constructions
 */
public class SpriteRenderer extends EntityRenderer {

    private Tileset tileset = null;
    protected int tile_id = 0;

    public SpriteRenderer(){
        tileset = new Tileset();
    }

    public Tileset get_tileset(){
        return tileset;
    }

    public void set_texture(String name){
        tileset.texture_name = name;
    }

    public void set_tile_id(int tile_id){
        this.tile_id = tile_id;
    }

    @Override
    public void render(){
        GL11.glTexParameteri(
                GL11.GL_TEXTURE_2D, 
                GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        
        GL11.glTexParameteri(
                GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_NEAREST);

        tileset.render_sprite(
            ent.origin.getX(),
            ent.origin.getY(),
            tile_id
        );
    }
}
