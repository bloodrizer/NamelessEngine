/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package render;

import java.util.Collections;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import ne.Game;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
/**
 *
 * 
 */
public class Render {
    private static java.util.Map<String,Texture> texture_cache = Collections.synchronizedMap(new java.util.HashMap<String,Texture>(32));


    public static Texture precache_texture(String name){
        try {
            /*Texture texture = TextureLoader.getTexture("PNG", new FileInputStream(
                Render.class.getResource(name).getPath()
            ));*/
            Texture texture = null;

            InputStream texture_stream = Render.class.getResourceAsStream(name);
            if (texture_stream != null){
                texture = TextureLoader.getTexture("PNG", texture_stream);
            }else{
                System.err.println("precache_texture('"+name+"') - InputStream is null!");
                Game.stop();
            }


            texture_cache.put(name, texture);
            return texture;
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage() + '('+ Render.class.getResource(name).getPath()+ ')');
            //System.exit(0);
            Game.running = false;
        }
        return null;
    }

    public static Texture get_texture(String name){
        Texture texture = texture_cache.get(name);
        
        if(texture != null){
            return texture;
        }else{
            return precache_texture(name);
        }
    }

    public static void bind_texture(String name){
        Texture texture = get_texture(name);
        if (texture != null){
            texture.bind();
        }
    }
}
