/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

import events.network.EPlayerLogout;
import game.modes.IGameMode;
import game.modes.ModeInGame;

import org.lwjgl.opengl.Display;

import game.modes.ModeMainMenu;
import java.awt.Canvas;
import java.util.Collections;
import java.util.EnumMap;
import ne.io.Io;
import ne.ui.NE_GUI_Element;
import ne.ui.NE_GUI_System;

import org.lwjgl.opengl.GL11;
import player.Player;
import render.WindowRender;
import ui.IUserInterface;
import world.Timer;

/**
 *
 * @author Administrator
 */
public class Game {

    public enum GameModes {
        Undefined, MainMenu, InGame
    };

    static GameModes state = GameModes.Undefined;

    public void set_state(GameModes state){
        Game.state = state;
    }


    private static java.util.Map<GameModes,IGameMode> game_modes = 
            Collections.synchronizedMap(new EnumMap<GameModes,IGameMode>(GameModes.class));

    public static IGameMode get_game_mode(){

        IGameMode __mode = game_modes.get(Game.state);
        if (__mode!= null){
            return __mode;
        }

        switch(Game.state){
            case InGame:
                __mode = new ModeInGame();
            break;

            case MainMenu:
                __mode = new ModeMainMenu();
            break;

            case Undefined:
                System.out.println("Undefined game state - unable to run");
            break;
        }

         game_modes.put(Game.state, __mode);
         __mode.run();

         IUserInterface gameUI = __mode.get_ui();
         gameUI.build_ui();

         return __mode;
    }

    public static Canvas display_parent = null;
    public static void set_canvas(Canvas display_parent){
        Game.display_parent = display_parent;
    }

    //TODO: refact me
    public static boolean running = true;

    public void run(){
        IGameMode mode = null;

        Io.init();  //init io layer

        try {
            WindowRender.create();

            while(running) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                mode = Game.get_game_mode();

                

               Timer.tick(); 
               mode.update();
             
               Display.sync(60);
               Display.update();

               if (Display.isCloseRequested()){  
                    if (Player.get_ent() != null){
                        EPlayerLogout event = new EPlayerLogout();
                        event.post();
                    }
                    running = false;
               }
            }

            

            System.out.println("Game stopped, destroying lwjgl render...");
            
            //gui.destroy();
            WindowRender.destroy();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static void stop() {
        running = false;
    }

    /*
     * Helper function, returning root gui element of current game mode
     * Abviously, as you can see below, it is very usefull;
     */
    public static NE_GUI_Element get_ui_root(){
        return get_ui().root;
    }

    public static NE_GUI_System get_ui() {
        return Main.game.get_game_mode().get_ui().get_nge_ui();
    }
}
