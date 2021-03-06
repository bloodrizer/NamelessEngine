/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import client.ClientGameEnvironment;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.controller.IEntityController;
import game.ent.controller.NpcController;
import java.util.ArrayList;
import ne.Input;
import ne.Main;
import net.sf.ehcache.Cache;
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

        
        
        //-----------------------------------

        OverlaySystem.ttf.drawString(10, 70, "FPS: " + Integer.toString( Timer.get_fps() ), Color.white);

        OverlaySystem.ttf.drawString(10, 90, "Camera @: " +
                (int)WorldViewCamera.camera_x +
                "," + 
                (int)WorldViewCamera.camera_y +
                " - " + WorldViewCamera.target.toString()
                ,Color.white);
        
        OverlaySystem.ttf.drawString(WindowRender.get_window_w()-100 , 10, "z-index: " + WorldView.get_zindex(), Color.white);


        //a little bit of cache debug


        Cache srvCache = Main.neServer.getCacheManager().getCache("chunkCache");
        if(srvCache==null){
            return;
        }
        OverlaySystem.ttf.drawString(10, 110, "Server cache: size - " +
                srvCache.getSize() +
                ", mem - " +
                srvCache.getMemoryStoreSize() +
                ", disk - " +
                srvCache.getDiskStoreSize()
                ,Color.white);
    }

    public static void debugPathfinding() {
        if (!Input.key_state_alt){
            return;
        }


        Point tileFrom = new Point(0,0);
        Point tileTo = new Point(0,0);

        Entity[] entList = ClientGameEnvironment.getEntityManager().getEntities(WorldView.get_zindex());
        for (Entity ent: entList){
            IEntityController controller = ent.controller;
            //SO FAR WE ONLY DEBUG PLAYER
            if (controller != null && controller instanceof NpcController){
                NpcController npc_controller = (NpcController)controller;

                if (npc_controller.path == null){
                    continue;
                }
                Step prevStep = new Step(ent.origin.getX(), ent.origin.getY());

                for (int i=0; i<npc_controller.path.steps.size();i++){

                    Step step = (Step)npc_controller.path.steps.get(i);

                    tileFrom.setLocation(prevStep);
                    if (i>0){
                        //root step is entity origin, which is allready in world coord system. So we do not recalculdate it.
                        tileFrom = ClientGameEnvironment.getWorldLayer(WorldView.get_zindex()).tile_map.local2world(tileFrom);
                    }

                    tileTo.setLocation(step);
                    tileTo = ClientGameEnvironment.getWorldLayer(WorldView.get_zindex()).tile_map.local2world(tileTo);

                    if (ent == Player.get_ent()){
                        OverlaySystem.drawLine(tileFrom, tileTo, Color.red);
                    }else{
                        OverlaySystem.drawLine(tileFrom, tileTo, Color.green);
                    }

                    prevStep = step;
                }
            }
        }
    }

}
