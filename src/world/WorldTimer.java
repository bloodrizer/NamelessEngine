/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import java.util.Calendar;
import java.util.TimerTask;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Administrator
 */
public class WorldTimer {

    public static final Calendar datetime = Calendar.getInstance();
    static {
        //java.util.Timer timer = new java.util.Timer();
        datetime.set(Calendar.HOUR_OF_DAY, 4);
    }


    public static void tick(){
        datetime.add(Calendar.MINUTE,1);

        if(datetime.get(Calendar.MINUTE) == 0){
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
        }
    }

    public void stop_timer() {
        //timer.cancel();
    }

}

