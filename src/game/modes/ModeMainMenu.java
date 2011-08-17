/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import events.EventManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ne.Input;
import ne.io.Io;
import ne.ui.NE_GUI_System;
import render.Render;
import render.overlay.OverlaySystem;
import ui.IUserInterface;
import ui.MainMenuUI;

/**
 *
 * @author Administrator
 */
public class ModeMainMenu implements IGameMode {
    //private NE_GUI_System gui;
    private OverlaySystem overlay;

    public void run(){
        //gui = new NE_GUI_System();
        overlay = new OverlaySystem();

        Render.set_cursor("/render/ico_default.png");
    }

    public void update(){
        try {
            Io.update();
        } catch (IOException ex) {
            Logger.getLogger(ModeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        Input.update();
        EventManager.update();
        
        //gui.render();
        get_ui().update();
        get_ui().render();

        overlay.render();
    }

    IUserInterface wgt = null;
    public IUserInterface get_ui(){
        if (wgt!=null){
            return wgt;
        }
        wgt = new MainMenuUI();

        return wgt;
    }
}
