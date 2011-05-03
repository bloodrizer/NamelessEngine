/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

/**
 *
 * @Simple simple sprite renderer for inanimated objects like trees / constructions
 */
public class SpriteRenderer extends EntityRenderer {

    private Tileset tileset = null;
    private int tile_id = 0;

    public SpriteRenderer(){
        tileset = new Tileset();
    }

    public void set_texture(String name){
        tileset.texture_name = name;
    }

    public void set_tile_id(int tile_id){
        this.tile_id = tile_id;
    }

    @Override
    public void render(){
        tileset.render_sprite(
            ent.origin.getX(),
            ent.origin.getY(),
            tile_id
        );
    }
}
