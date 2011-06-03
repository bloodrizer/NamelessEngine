/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import game.ent.controller.NpcController;
import org.newdawn.slick.Color;
import player.Player;
import world.Timer;
import world.WorldViewCamera;

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

        //----------player debug------------

        /*if (Player.get_ent() != null){
            NpcController npc_controller = (NpcController)Player.get_ent().controller;

            OverlaySystem.ttf.drawString(10, 25, "Player dest:" + npc_controller.destination + " path:" +
                   npc_controller.path + ";" +
                   " Step:" + npc_controller.step
            , Color.white);
            
            OverlaySystem.ttf.drawString(10, 45, "ft: "
                    + ""+ Player.get_ent().frame_time_ms +
                    " ;nft:"+(Player.get_ent().next_frame-Timer.get_time())
            , Color.white);
        }*/
        //-----------------------------------

        OverlaySystem.ttf.drawString(10, 70, "FPS: " + Integer.toString( Timer.get_fps() ), Color.white);

        OverlaySystem.ttf.drawString(10, 90, "Camera @: " +
                Integer.toString( (int)WorldViewCamera.camera_x )+
                "," + 
                Integer.toString( (int)WorldViewCamera.camera_y ) +
                " - " + WorldViewCamera.target.toString()
                ,
        Color.white);
    }

}
