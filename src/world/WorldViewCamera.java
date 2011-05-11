/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;
import player.Player;
import render.Tileset;

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
        Point position = get_position();
       
        //position = WorldView.world2local(position);

        GL11.glTranslatef(-position.getX(), -position.getY() , 0);
    }

    public static Point get_position(){
        return new Point((int)camera_x, (int)camera_y);
    }

    public static void update(){
        if (follow_target){
                //todo: move to math

                //TODO: FIXME! HERE IS AN UGLY HACK!
               

                float target_x = (target.getX())*Tileset.TILE_SIZE +  Player.get_ent().dx*Tileset.TILE_SIZE;
                float target_y = (target.getY())*Tileset.TILE_SIZE +  Player.get_ent().dy*Tileset.TILE_SIZE;

                Point delta = WorldView.world2local(new Point((int)target_x,(int)target_y));
                target_x = delta.getX();
                target_y = delta.getY();


                float dx = target_x - 12*Tileset.TILE_SIZE - camera_x;
		float dy = target_y - 8*Tileset.TILE_SIZE -  camera_y;

                
                

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
