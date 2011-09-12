/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.controller.IEntityController;
import game.ent.controller.NpcController;
import java.util.ArrayList;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import player.Player;
import render.WindowRender;
import world.Timer;
import world.WorldModel;
import world.WorldTimer;
import world.WorldView;
import world.WorldViewCamera;
import world.util.astar.Step;

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

        OverlaySystem.ttf.drawString(10, 25, "time: "
                    + WorldTimer.datetime.getTime()
            , Color.white);

        //----------show pathfinding route for every entity (debug)------------
        
        Point tileFrom = new Point(0,0);
        Point tileTo = new Point(0,0);
        
        Entity[] entList = EntityManager.getEntities(WorldView.get_zindex());
        for (Entity ent: entList){
            IEntityController controller = ent.controller;
            //SO FAR WE ONLY DEBUG PLAYER
            if (ent == Player.get_ent() && controller != null && controller instanceof NpcController){
                NpcController npc_controller = (NpcController)controller;
                
                if (npc_controller.path == null){
                    continue;
                }
                
                if (npc_controller.path.steps.size() > 1){
                    System.out.println("drawing "+npc_controller.path.steps.size()+"debug steps");
                }
          
                Step prevStep = new Step(ent.origin.getX(), ent.origin.getY());
                //TODO: step[0] is fucking incorrect, replace it with origin
                
                for (Step step: (ArrayList<Step>)(npc_controller.path).steps){
                    
                    if (step == npc_controller.path.steps.get(0)){
                        continue;
                    }
                    
                    tileFrom.setLocation(prevStep);
                    tileFrom = WorldModel.getWorldLayer(WorldView.get_zindex()).tile_map.local2world(tileFrom);
                    
                    tileTo.setLocation(step);
                    tileTo = WorldModel.getWorldLayer(WorldView.get_zindex()).tile_map.local2world(tileTo);
                    
                    OverlaySystem.drawLine(tileFrom, tileTo, Color.red);
                    
                    prevStep = step;
                }
            }
        }
        
        //-----------------------------------

        OverlaySystem.ttf.drawString(10, 70, "FPS: " + Integer.toString( Timer.get_fps() ), Color.white);

        OverlaySystem.ttf.drawString(10, 90, "Camera @: " +
                Integer.toString( (int)WorldViewCamera.camera_x )+
                "," + 
                Integer.toString( (int)WorldViewCamera.camera_y ) +
                " - " + WorldViewCamera.target.toString()
                ,
        Color.white);
        
        OverlaySystem.ttf.drawString(WindowRender.get_window_w()-100 , 10, "z-index: " + WorldView.get_zindex(), Color.white);
    }

}
