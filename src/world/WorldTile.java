/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

/**
 *
 * @author Administrator
 */
public class WorldTile {
    private int tile_id = 0;
    private int height = 0;

    public int get_tile_id(){
        return tile_id;
    }

    public void set_tile_id( int tile_id){
        this.tile_id = tile_id;
    }

    public void set_height(int height){
        this.height = height;
    }

    public int get_height(){
        return height;
    }

    public WorldTile(int tile_id){
        this.tile_id = tile_id;
    }

}
