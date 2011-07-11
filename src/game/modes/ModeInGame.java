/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import events.EMouseClick;
import events.network.EPlayerLogon;
import events.Event;
import events.EventManager;
import game.ent.Entity;
import game.ent.EntityManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import player.Player;
import render.Tileset;
import ui.GameUI;
import world.WorldModel;

import events.IEventListener;
import game.combat.BasicCombat;
import game.ent.EntityPlayer;
import game.ent.controller.NpcController;
import game.ent.controller.PlayerController;
import game.ent.monsters.EntMonster;
import items.BaseItem;
import ne.Input;
import ne.Input.MouseInputType;
import ne.effects.EffectsSystem;
import ne.io.Io;
import ne.ui.NE_GUI_System;
import org.lwjgl.opengl.GL11;
import render.overlay.OverlaySystem;
import ui.IUserInterface;
import world.Timer;
import world.WorldTile;
import world.WorldView;
import world.WorldViewCamera;

/**
 *
 * @author Administrator
 */
public class ModeInGame implements IGameMode, IEventListener {

    //private Tileset tileset = null;

    private Tileset bg_tileset;
    private WorldView  view;
    private WorldModel model;
    private NE_GUI_System gui;
    private EffectsSystem fx;



    private OverlaySystem overlay;
    
    public ModeInGame(){
        EventManager.subscribe(this);
    }

    public void run(){
        bg_tileset = new Tileset();
        view  = new WorldView();
        model = new WorldModel();

        overlay = new OverlaySystem();

        fx = new EffectsSystem();

        //gui = new NE_GUI_System();

        Timer.init();   //very-very critical

        //synchronize with server
        //init world

        
    }

    void spawn_player(Point location){
        Entity player_ent = new EntityPlayer();
        EntityManager.add(player_ent);
        player_ent.spawn(12345, location);

        WorldViewCamera.target.setLocation(location);

        player_ent.set_controller(new PlayerController());
        //player_ent.set_combat(new BasicCombat());

        Player.set_ent(player_ent);
    }




    public void update(){
        try {
            Io.update();
        } catch (IOException ex) {
            Logger.getLogger(ModeInGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        Input.update();
        EventManager.update();
        model.update();

        fx.update();

        fx.render();
        view.render();

        get_ui().get_nge_ui().render();

        overlay.render();

        
        
    }

    IUserInterface ui = null;
    public IUserInterface get_ui(){
        //return new GameUI();
        if (ui == null){
            ui = new GameUI();
        }
        return ui;
    }

    //--------------------------------------------------------------------------
    public void e_on_event(Event event){

       if (event instanceof EPlayerLogon){
           spawn_player(((EPlayerLogon)event).origin);
       }
       else if(event instanceof EMouseClick){
           e_on_mouse_click(((EMouseClick)event));
       }
       else
       {
           //System.out.println("Unknown message registered:" + event.classname());   //debug
       }
    }

    public void e_on_mouse_click( EMouseClick event){
        Point tile_origin = view.getTileCoord(event.origin);

        //System.out.println(tile_origin);
        if (event.type == MouseInputType.LCLICK) {
            WorldTile tile = WorldModel.get_tile(tile_origin.getX(), tile_origin.getY());
            
            if(tile == null){
                return;
            }

            Entity ent = tile.get_actor();
            if (ent != null && ent instanceof EntMonster){
                Player.attack(ent);
            }else{
                Player.move(tile_origin);
            }
        }
        //todo: use Player.player_ent.controller.set_target(tile_origin);
    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
        
    }

    private Object EPlayerLogon(Event event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
