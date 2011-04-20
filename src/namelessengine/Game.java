/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package namelessengine;


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

import org.lwjgl.opengl.GL11;
import render.Render;
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

    public static IGameMode get_game_mode(){
        IGameMode __mode = null;

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

         return __mode;
    }

    public void run(){
        IGameMode mode = Game.get_game_mode();

        try {
            Render.create();
            LWJGLRenderer renderer = new LWJGLRenderer();
            renderer.setUseSWMouseCursors(true);

            Widget gameUI = mode.get_ui();

            if (gameUI == null){
                System.out.println("Unable to assign game UI");
                gameUI = new DefaultUI();
            }

            GUI gui = new GUI(gameUI, renderer);

            ThemeManager themeManager = ThemeManager.createThemeManager(this.getClass().getResource("simple.xml"), renderer);
            gui.applyTheme(themeManager);

            mode.run();

            IUserInterface ui = (IUserInterface) gameUI;
            while(!Display.isCloseRequested() && !ui.quit) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                mode.update();
                gui.update();
                Display.update();
            }
            
            gui.destroy();
            Render.destroy();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
