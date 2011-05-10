/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

/*
 * Composite npc renderer
 *
 *
 * It features basic animation and nwse sprite orientation
 *
 *
 *
 *
 *
 */


public class NPCRenderer extends EntityRenderer{

    private Tileset tileset = null;

    private int ANIMATION_LENGTH = 0;
    private int frame_id = 0;

    public NPCRenderer(){
        tileset = new Tileset();
    }

    public Tileset get_tileset(){
        return tileset;
    }

    public void set_texture(String name){
        tileset.texture_name = name;
    }

    public void set_animation_length(int frames){
        this.ANIMATION_LENGTH = frames;

         get_tileset().TILESET_W = ANIMATION_LENGTH;
         get_tileset().TILESET_H = 4;
    }

    private int get_tile_id(){
        int tile_id = 0;
        switch(ent.orientation){
            case ORIENT_N:
                tile_id = 0;
            break;
            case ORIENT_E:
                tile_id = 8;
            break;
            case ORIENT_S:
                tile_id = 16;
            break;
            case ORIENT_W:
                tile_id = 24;
            break;
        }

        tile_id = tile_id+frame_id;
        return tile_id;
    }

    @Override
    public void render(){
        tileset.render_sprite(
            ent.origin.getX(),
            ent.origin.getY(),
            get_tile_id()
        );
    }
}
