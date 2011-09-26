/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import client.ClientEventManager;
import client.NettyClient;
import events.EPlayerAuthorise;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.ESelectCharacter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ne.Input;
import ne.io.Io;
import ne.ui.NE_GUI_System;
import player.CharacterInfo;
import render.Render;
import render.overlay.OverlaySystem;
import render.overlay.VersionOverlay;
import ui.IUserInterface;
import ui.MainMenuUI;

/**
 *
 * @author Administrator
 */
public class ModeMainMenu implements IGameMode, IEventListener {

    //public static final boolean FORCE_AUTOLOGIN = true;    //USE THIS ONLY FOR DEBUG

    //private NE_GUI_System gui;
    private OverlaySystem overlay;
    
    public ModeMainMenu(){
        ClientEventManager.eventManager.subscribe(this);
    }

    public void run(){
        //gui = new NE_GUI_System();
        overlay = new OverlaySystem(){
            @Override
            public void render() {
                VersionOverlay.render();
            }

        };

        Render.set_cursor("/render/ico_default.png");
        
        /*if (FORCE_AUTOLOGIN){
            try {
                Io.reset();
                Io.connect();
                Io.login("Red", "Password");
            } catch (SocketTimeoutException ex) {
                Logger.getLogger(ModeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ModeMainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            //NettyClient.connect();
        }*/
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

    public void e_on_event(Event event) {
        /* (event instanceof EPlayerAuthorise && FORCE_AUTOLOGIN){
            event.dispatch();   //TODO: check if it would conflict with gui subsystem
            
            CharacterInfo chrInfo = new CharacterInfo();
            chrInfo.name = "PlayerName(debug)";
            
            ESelectCharacter selectChrEvent = new ESelectCharacter(chrInfo);
            selectChrEvent.post();
        }*/
    }

    public void e_on_event_rollback(Event event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
