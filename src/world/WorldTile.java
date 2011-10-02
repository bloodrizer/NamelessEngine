/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import game.ent.EntityActor;
import game.ent.decals.EntDecalBlood;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Administrator
 */
public class WorldTile {
    public float light_level = 0.0f;
    public Point origin = null;


    static BufferedImage colorLut;

    static {
        try {
            colorLut = ImageIO.read(ResourceLoader.getResource("render/textures/grasscolor.png").openStream());
        } catch (IOException ex) {
            throw new RuntimeException("failed to load grass color mask");
        }
    }


    public Entity get_active_object() {

        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity ent = (Entity)list[i];

            if (ent.get_action_list().size() > 0){
                return ent;
            }
        }
        return null;
    }
    
    
    public WorldTile(int tile_id){
        this.tile_id = tile_id;
    }


    public Vector4f colorForTemperatureAndHumidity() {

        double temp = (double)height / 255.0f;
        double hum = moisture / 10.0f;

        //hum *= temp;
        int rgbValue = colorLut.getRGB((int) ((1.0 - temp) * 255.0f), (int) ((1.0 - hum) * 255.0f));

        Color c = new Color(rgbValue);
        //return new Vector4f((float) c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 1.0f);
        return new Vector4f((float) c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f, 1.0f);
    }



    public enum TerrainType {
        TERRAIN_FORREST,
        TERRAIN_PLAIN,
        TERRAIN_WATER,
        TERRAIN_ROCK    //Valid for undeground areas only
    }

    public float moisture = 0.0f;

    public enum BiomeType {

        BIOME_SNOW,
        BIOME_TUNDRA(0),
        BIOME_BARE,
        BIOME_SCORCHED,

        BIOME_TAIGA(9),
        BIOME_SHRUBLAND,
        BIOME_TEMP_DESERT(2),

        BIOME_TEMP_RAINFOREST(25),
        BIOME_DECIDUOS_FOREST(25),
        BIOME_GRASSLAND(0),

        BIOME_TROPICAL_RAINFOREST(25),
        BIOME_SEASONAL_FOREST(25),
        BIOME_SUBTROPICAL_DESERT(2),
        
        BIOME_SHORE(2);     //shore is small sand one-tile area near the water source

        private final int tile_id;
        BiomeType(int tile_id){
            this.tile_id = tile_id;
        }
        BiomeType(){
            this.tile_id = 0;
        }

        public int tile_id(){
            return tile_id;
        }

        public int get_zindex(){
            return tile_id; //temp shit, change later
        }
    }

    private int tile_id = 0;
    private int height = 0;

    public TerrainType terrain_type = TerrainType.TERRAIN_PLAIN;
    public BiomeType biome_type = BiomeType.BIOME_GRASSLAND;


    public int get_moisture_zone(){
        int moisture_zone = (int) (moisture / (5.0f / 4)) + 1;

        return moisture_zone;
    }

    public int get_elevation_zone(){
        return height / (255/4) + 1;
    }

    public void update_biome_type(){
        int elevation_zone = get_elevation_zone();

        int moisture_zone = get_moisture_zone();

        switch(elevation_zone){
            case 1:
                switch(moisture_zone) {
                    case 1:
                        biome_type = BiomeType.BIOME_SUBTROPICAL_DESERT;
                    break;
                    case 2:
                        biome_type = BiomeType.BIOME_GRASSLAND;
                    break;
                    case 3:
                        biome_type = BiomeType.BIOME_SEASONAL_FOREST;
                    break;
                    case 4:
                        biome_type = BiomeType.BIOME_SEASONAL_FOREST;
                    break;
                    case 5:
                        biome_type = BiomeType.BIOME_TROPICAL_RAINFOREST;
                    break;
                }
            break;
            case 2:
                switch(moisture_zone) {
                    case 1:
                        biome_type = BiomeType.BIOME_TEMP_DESERT;
                    break;
                    case 2:
                        biome_type = BiomeType.BIOME_GRASSLAND;
                    break;
                    case 3:
                        biome_type = BiomeType.BIOME_GRASSLAND;
                    break;
                    case 4:
                        biome_type = BiomeType.BIOME_DECIDUOS_FOREST;
                    break;
                    case 5:
                        biome_type = BiomeType.BIOME_TEMP_RAINFOREST;
                    break;
                }
            break;
            case 3:
                switch(moisture_zone) {
                    case 1:
                        biome_type = BiomeType.BIOME_TEMP_DESERT;
                    break;
                    case 2:
                        biome_type = BiomeType.BIOME_TEMP_DESERT;
                    break;
                    case 3:
                        biome_type = BiomeType.BIOME_SHRUBLAND;
                    break;
                    case 4:
                        biome_type = BiomeType.BIOME_SHRUBLAND;
                    break;
                    case 5:
                        biome_type = BiomeType.BIOME_TAIGA;
                    break;
                }
            break;
            case 4:
                switch(moisture_zone) {
                    case 1:
                        biome_type = BiomeType.BIOME_SCORCHED;
                    break;
                    case 2:
                        biome_type = BiomeType.BIOME_BARE;
                    break;
                    case 3:
                        biome_type = BiomeType.BIOME_TUNDRA;
                    break;
                    case 4:
                        biome_type = BiomeType.BIOME_SNOW;
                    break;
                    case 5:
                        biome_type = BiomeType.BIOME_SNOW;
                    break;
                }
            break; 
        }

        return;
    }

    public float get_speed_modifier(){
        float speed = 1.0f;
        switch (biome_type){
            case BIOME_TEMP_DESERT:
                speed = 0.4f;    //-60%
            break;
            case BIOME_SUBTROPICAL_DESERT:
                speed = 0.4f;    //-60%
            break;
            case BIOME_DECIDUOS_FOREST:
                speed = 0.6f;   //-40%
            break;
        }

        if (terrain_type == TerrainType.TERRAIN_WATER){
            speed = 0.3f;   //-70%
        }

        return speed;
    }

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

    Entity getEntity(Class<EntDecalBlood> ent_class) {
        Object[] list = ent_list.toArray();
        for(int i=ent_list.size()-1; i>=0; i--){
            Entity ent = (Entity)list[i];

            if (ent_class.isInstance(ent)){
                return ent;
            }
        }
        return null;
    }

    public boolean is_blocked(){

        if (terrain_type == TerrainType.TERRAIN_WATER){
            return true;
        }
        //TODO: allow to move in a shale water? (low depth)
        //TODO: show message in the deep water
        
        if (terrain_type == TerrainType.TERRAIN_ROCK){
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


}
