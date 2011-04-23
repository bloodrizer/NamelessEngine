/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import org.newdawn.slick.Color;
import world.Timer;

public class DebugOverlay {

    public static void render(){
        long total = Runtime.getRuntime().totalMemory();
        long free  = Runtime.getRuntime().freeMemory();

        OverlaySystem.ttf.drawString(10, 5,
                "Memory: " +
                    String.format("%.2f", free /(1024.0f*1024)
                    )+
                "MB free of " +
                    Float.toString( total/(1024.0f*1024) ) +
                "MB"
        , Color.white);

        /*OverlaySystem.ttf.drawString(10, 25, Long.toString(
                Timer.get_time()
                )
        , Color.white);*/

        OverlaySystem.ttf.drawString(10, 55, "FPS: " + Integer.toString( Timer.get_fps() ), Color.white);
    }

}
