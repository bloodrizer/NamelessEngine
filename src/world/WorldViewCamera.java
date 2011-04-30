/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import render.TilesetRender;

/**
 *
 * @author Administrator
 */
public class WorldViewCamera {
    public static float camera_x = 0;
    public static float camera_y = 0;

    public static Point target = new Point(0,0);
    public static float speed = 1.0f;

    public static boolean follow_target = true;

    public static void move(float dx, float dy){
        camera_x += dx*1.5;
        camera_y += dy*1.5;
    }
    public static void set(float x, float y){
        camera_x = x;
        camera_y = y;
    }

    public static void setMatrix(){
        GL11.glTranslatef(-camera_x, -camera_y, 0);
    }

   
    public static void update(){
        if (follow_target){
                //todo: move to math

                float target_x = target.getX() - 12;
                float target_y = target.getY() - 8;


                float dx = target_x * TilesetRender.TILE_SIZE - camera_x;
		float dy = target_y * TilesetRender.TILE_SIZE - camera_y;
		double distance = Math.sqrt(dx * dx + dy * dy);

		//normalize it to length 1 (preserving direction), then round it and
		//convert to integer so the movement is restricted to the map grid
                float acceleration = 0.0f;
                if (distance > 10){
                    acceleration = 5.0f;
                }
                if (distance < 2){
                    acceleration = 0.0f;
                }

                dx = (float) (Math.round(dx / distance) * (distance/4));
                dy = (float) (Math.round(dy / distance) * (distance/4));

                move(dx,dy);
        }
    }

    
}
