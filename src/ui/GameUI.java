/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Menu;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import de.lessvoid.nifty.tools.SizeValue;
import events.EKeyPress;
import events.EMouseClick;
import events.Event;
import events.EventManager;
import events.IEventListener;
import game.ent.Entity;
import game.ent.IEntityAction;
import ne.Input.MouseInputType;
import ne.ui.NE_GUI_Inventory;
import ne.ui.NE_GUI_Popup;
import ne.ui.NE_GUI_System;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import player.Player;
import world.WorldModel;
import world.WorldTile;
import world.WorldView;


/**
 *
 * @author Administrator
 */
public class GameUI implements IUserInterface,  IEventListener {
    private static Screen screen = null;

    public GameUI(){
        //NOTE: using constructor of UI is deprecated. Use build_ui instead
        
    }

    NE_GUI_Inventory inventory;


    public void build_ui(){
        EventManager.subscribe(this);
        ui = new NE_GUI_System();



        inventory = new NE_GUI_Inventory();
        ui.root.add(inventory);
        inventory.set_container(Player.get_ent().container);

    }
    //public UIItemContainer inventory;   //test shit
    //--------------------------------------------------------------------------

    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.screen = screen;

    }

   
    public static Element popup;

    public class GameUIItem {
        public GameUIItem(String name){
            
        }
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
        IEntityAction[] action_list = ent.get_action_list();

        System.out.println("Fetched "+Integer.toString(action_list.length)+"actions");
        for(int i=0; i<action_list.length; i++){
            //popupMenu.addMenuItem(action_list[i].get_name(), "ui/branch_ico.png", action_list[i]);
            __popup.add_item(action_list[i]);
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


    NE_GUI_System ui;
    public NE_GUI_System get_nge_ui() {
        return ui;
    }
}
