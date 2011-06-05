/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import actions.IAction;

import events.EKeyPress;
import events.EMouseClick;
import events.Event;
import events.EventManager;
import events.IEventListener;
import game.ent.Entity;
import game.ent.IEntityAction;
import items.BaseItemAction;
import java.util.ArrayList;
import java.util.Iterator;
import ne.Input.MouseInputType;
import ne.ui.NE_GUI_Craft;
import ne.ui.NE_GUI_Inventory;
import ne.ui.NE_GUI_Popup;
import ne.ui.NE_GUI_QuickslotBar;
import ne.ui.NE_GUI_System;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import player.Player;
import render.WindowRender;
import world.WorldModel;
import world.WorldTile;
import world.WorldView;


/**
 *
 * @author Administrator
 */
public class GameUI implements IUserInterface,  IEventListener {

    public static NE_GUI_Inventory inventory;
    public static NE_GUI_Craft craft;


    public GameUI(){
        //NOTE: using constructor of UI is deprecated. Use build_ui instead
    }

/*
 *
 * Create all game controls - menus, inventory, slots, etc.
 *
 */
    public void build_ui(){
        EventManager.subscribe(this);
        ui = new NE_GUI_System();


        
       
        NE_GUI_QuickslotBar quickslots = new NE_GUI_QuickslotBar();
        ui.root.add(quickslots);

        quickslots.x = WindowRender.get_window_w() / 2  - quickslots.w /2;
        quickslots.y = WindowRender.get_window_h() - quickslots.h - 10;
        
        /*
         * Inventory goes after quickslots, as items of inventory should have higher 
         * z-axis index
         * Otherwise quickslot drag-n-drop would look lame
         */

        inventory = new NE_GUI_Inventory();
        ui.root.add(inventory);

        inventory.x = 10;
        inventory.y = 130;

        craft = new NE_GUI_Craft();
        ui.root.add(craft);

        craft.x = 10;
        craft.y = 300;


    }

    public void e_on_event(Event event) {
        if (event instanceof EKeyPress){
  
            EKeyPress key_event = (EKeyPress) event;
            switch(key_event.key){
                case Keyboard.KEY_F1:
                    toggle_console();
                break;
                case Keyboard.KEY_I:
                    toggle_inventory();
                break;
                case Keyboard.KEY_Q:
                    toggle_craft();
                break;
                case Keyboard.KEY_DOWN:
                    WorldView.ISOMETRY_TILE_SCALE -= 0.1f;
                break;
                case Keyboard.KEY_UP:
                    WorldView.ISOMETRY_TILE_SCALE += 0.1f;
                break;
            }
        }else if(event instanceof EMouseClick){
           //e_on_mouse_click(((EMouseClick)event));
            EMouseClick click_event = (EMouseClick)event;
            if (click_event.type == MouseInputType.RCLICK){
                context_popup(click_event);
            }
       }
    }

    public void context_popup(EMouseClick event){

        class DropItem extends BaseItemAction{

            @Override
            public void execute() {
                System.out.println("ActionCutTree");
                System.out.print(get_owner());
            }

        }

        Point tile_origin = WorldView.getTileCoord(event.origin);
        WorldTile tile = WorldModel.get_tile(tile_origin.getX(), tile_origin.getY());

        Entity ent = tile.get_obstacle();
        if (ent == null){
            System.out.println("unable to get obstacle for tile");
            return;
        }

        NE_GUI_Popup __popup = new NE_GUI_Popup();

        ui.root.add(__popup);
        __popup.x = event.origin.getX();
        __popup.y = event.get_window_y();

        //-------------------------------------------------
        ArrayList action_list = ent.get_action_list();
        //IAction<Entity>[] actions = (IAction<Entity>[]) action_list.toArray();
        Iterator<IAction> itr = action_list.iterator();

        System.out.println("Fetched "+Integer.toString(action_list.size())+" actions");


        while (itr.hasNext()){
            IAction element = itr.next();
            __popup.add_item(element);
        }
        
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void toggle_console(){
        /*Element element = screen.findElementByName("console");
        element.setVisible(!element.isVisible());*/
    }

    public void toggle_inventory(){
        inventory.visible = !inventory.visible;
    }
    
    public void toggle_craft(){
        craft.visible = !craft.visible;
    }


    NE_GUI_System ui;
    public NE_GUI_System get_nge_ui() {
        return ui;
    }
}
