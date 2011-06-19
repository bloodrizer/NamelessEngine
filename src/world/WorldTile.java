/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import game.ent.EntityActor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class WorldTile {
    public float light_level = 0.0f;

    public enum TerrainType {
        TERRAIN_FORREST,
        TERRAIN_PLAIN,
        TERRAIN_WATER
    }

    private int tile_id = 0;
    private int height = 0;

    public TerrainType terrain_type = TerrainType.TERRAIN_PLAIN;

    //allocate sum data for entity storing
    public List<Entity> ent_list = new ArrayList<Entity>(10);

    public void add_entity(Entity ent){
        ent_list.add(ent);
        ent.tile = this;
    }
    public void remove_entity(Entity ent){
        ent_list.remove(ent);
    }

    /*
     * Check if tile has entity assigned to it
     *
     */
    public boolean has_ent(Class ent_class){
        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity ent = (Entity)list[i];

            if (ent_class.isInstance(ent)){
                return true;
            }
        }
        return false;
    }

    public boolean is_blocked(){

        if (terrain_type == TerrainType.TERRAIN_WATER){
            return true;
        }


        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity entity = (Entity)list[i];
            if (entity.is_blocking()){
                  return true;
            }
        }
        return false;
    }

    //TODO: change to get_world_actor

    //this is more safe and more useful version of get_obstacle
    public Entity get_actor(){
        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity entity = (Entity)list[i];
            if (entity instanceof EntityActor){
                return entity;
            }
        }
        return null;
    }

    public Entity get_obstacle(){
        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity entity = (Entity)list[i];
            if (entity.is_blocking()){
                return entity;
            }
        }
        return null;
    }

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
