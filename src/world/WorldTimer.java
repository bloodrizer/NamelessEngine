/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.controller.NpcController;
import game.ent.monsters.Zombie;
import java.util.Calendar;
import java.util.TimerTask;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector3f;
import player.Player;

/**
 *
 * @author Administrator
 */
public class WorldTimer {

    public static final Calendar datetime = Calendar.getInstance();
    static {
        //java.util.Timer timer = new java.util.Timer();
        datetime.set(Calendar.HOUR_OF_DAY, 1);
        datetime.set(Calendar.SECOND, 0);
    }


    public static void tick(){
        datetime.add(Calendar.SECOND,15);

        if(datetime.get(Calendar.MINUTE) == 0 && datetime.get(Calendar.SECOND) == 0){
            e_on_new_hour();
        }
    }

    public static float get_light_amt(){

        float hour = datetime.get(Calendar.HOUR_OF_DAY) + datetime.get(Calendar.MINUTE)/60.0f;
        float amt = 1.0f;

        if (hour < 7 || hour >= 21){
            amt = 0.0f;
        }
        if ( hour >=7 && hour <= 10  ) {
            amt = (hour-7)/3.0f;
        }
        if ( hour >= 17 && hour < 21){
            amt = (21.0f-hour)/5.0f;
        }
   
        amt = amt/2.0f;

        return amt;
    }

    public static boolean is_night(){
       float hour = datetime.get(Calendar.HOUR_OF_DAY) + datetime.get(Calendar.MINUTE)/60.0f;
       return (hour < 7 || hour >= 21);
    }

    private static void e_on_new_hour() {
        if (is_night()){
            //there is slight chance of spawning zombie each hour

            //TODO: check if camera is not centered on this area and spawn a zombie
            //if !(WorldCamera.tile_in_fov()){ //etc

            int chance = (int)(Math.random()*100);
            if(chance < 90 ){
                Zombie zombie = new Zombie();
                zombie.spawn(new Point(
                        Player.get_ent().origin.getX() + (int)(Math.random()*20-10),
                        Player.get_ent().origin.getY() +(int)(Math.random()*20-10)
                    )
                );
                //zombie.set_controller(new NpcController());
            }
        }
    }

    public void stop_timer() {
        //timer.cancel();
    }

}

