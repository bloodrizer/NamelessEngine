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

    public static Vector3f get_sun_color(){
        
        float hour = datetime.get(Calendar.HOUR_OF_DAY) + datetime.get(Calendar.MINUTE)*0.01f;

        if ( hour <= 7 ) {
            return new Vector3f(0.0f,0.0f,0.0f);
        }

        if ( hour > 7 && hour <= 17 ) {
            float percentage = (
                    (hour-7) / 10
                  );

            float r = ( 0.0f + 1.0f * percentage );
            float g = ( 0.0f + 1.0f * percentage );
            float b = ( 0.0f + 0.765f * percentage );

            return new Vector3f(r,g,b);
        }

        if ( hour > 17 && hour < 21){

            float percentage = (
                    (hour-17) / 4
                  );

            float r = 1.0f;
            float g = 1.0f - ( 0.235f + 0.765f * percentage );
            //float g = 1.0f - ( 0.0f + 0.765f * percentage );
            float b = 0.882f - ( 0.235f + 0.647f * percentage );
            //float b = 0.765f - ( 0.0f + 0.647f * percentage );

            return new Vector3f(r,g,b);

        }

        if ( hour >= 21){

            float percentage = (
                    (hour-17) / 4
                  );

            float r = 1.0f - ( 0.235f + 0.647f * percentage );
            float g = 0.235f - ( 0.235f * percentage );
            float b = 0.235f - ( 0.235f * percentage );

            return new Vector3f(r,g,b);

        }

        return new Vector3f(0.2f,0.2f,0.2f);

    }

    public void stop_timer() {
        //timer.cancel();
    }

}

