/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;


import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import game.modes.IGameMode;
import game.modes.ModeDefault;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import game.modes.ModeMainMenu;
import java.util.Collections;
import java.util.EnumMap;

import org.lwjgl.opengl.GL11;
import render.WindowRender;
import ui.DefaultUI;
import ui.GameUI;
import ui.IUserInterface;
import ui.MainMenuUI;

/**
 *
 * @author Administrator
 */
public class Game {
    public enum GameModes {
        Undefined, MainMenu, Default
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
            case Default:
                __mode = new ModeDefault();
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
         //nifty.

         IUserInterface gameUI = __mode.get_ui();
         gameUI.build_ui(nifty);

         return __mode;
    }

    public static Nifty nifty = null;
    public void run(){
        IGameMode mode = null;

        try {
            WindowRender.create();

            nifty = new Nifty(
                new LwjglRenderDevice(),
                new OpenALSoundDevice(),
                new LwjglInputSystem(),
                new TimeProvider()
            );

            /*IUserInterface gameUI = mode.get_ui();

            if (gameUI == null){
                System.out.println("Unable to assign game UI");
                //gameUI = new DefaultUI();
            }
            gameUI.build_ui(nifty);*/

            while(!Display.isCloseRequested() ) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                //update it ftw
                mode = Game.get_game_mode();
                mode.update();


                if (nifty.render(false)){
                    //end of nifty render
                }

                //gui.update();
                Display.update();
            }
            
            //gui.destroy();
            WindowRender.destroy();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
