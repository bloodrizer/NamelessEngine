/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package render;

import java.util.Collections;

import java.io.FileInputStream;
import java.io.IOException;
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
            
            Texture texture = TextureLoader.getTexture("PNG", Render.class.getResourceAsStream(name));

            //texture.

            System.out.println(name+" 's alpha:");
            System.out.println(texture.hasAlpha());

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
