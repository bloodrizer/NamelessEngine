/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import events.EventManager;
import ne.Input;
import ne.io.Io;
import ne.ui.NE_GUI_System;
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
    }

    public void update(){
        Io.update();

        Input.update();
        EventManager.update();
        
        //gui.render();
        get_ui().get_nge_ui().render();

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
