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
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Point;
import world.WorldModel;
import world.WorldTile;
import world.WorldView;


/**
 *
 * @author Administrator
 */
public class GameUI implements IUserInterface, ScreenController, IEventListener {
    private static Screen screen = null;

    public GameUI(){
        //NOTE: using constructor of UI is deprecated. Use build_ui instead
    }

    Nifty nifty;
    public void build_ui(Nifty nifty){
        EventManager.subscribe(this);

        /*this.nifty = nifty;

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        ScreenBuilder screen_builder = new ScreenBuilder("start");
        screen_builder.controller(this);
        
        LayerBuilder ui_layer = new LayerBuilder("game_ui_layer");
        ui_layer.childLayoutAbsolute();

        screen_builder.layer(ui_layer);

        Screen ui_screen = screen_builder.build(nifty);
        nifty.addScreen("start", ui_screen);
        nifty.gotoScreen("start");
        //----------------------------------------------------------------------

        inventory = new UIItemContainer(ui_layer);  //<-add a window
        inventory.build(nifty, screen);
        inventory.set_title("Inventory");
        //inventory.show();*/
    }
    public UIItemContainer inventory;   //test shit
    //--------------------------------------------------------------------------

    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.screen = screen;

    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

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

        /*Point tile_origin = WorldView.getTileCoord(event.origin);
        WorldTile tile = WorldModel.get_tile(tile_origin.getX(), tile_origin.getY());

        Entity ent = tile.get_obstacle();
        if (ent == null){
            System.out.println("unable to get obstacle for tile");
            return;
        }

        this.popup = nifty.createPopup("niftyPopupMenu");

        Menu<IEntityAction> popupMenu =
        popup.findNiftyControl("#menu", Menu.class);
        popupMenu.setWidth(new SizeValue("250px"));
        

        //-------------------------------------------------
        IEntityAction[] action_list = ent.get_action_list();

        System.out.println("Fetched "+Integer.toString(action_list.length)+"actions");
        for(int i=0; i<action_list.length; i++){
            popupMenu.addMenuItem(action_list[i].get_name(), "ui/branch_ico.png", action_list[i]);
        }
        //-------------------------------------------------*/

         // this is required and is not happening automatically!
        //popupMenu.addMenuItem("Cut tree", "ui/axe_ico.png", new GameUIItem("Something"));
        //popupMenu.addMenuItemSeparator();
        //popupMenu.addMenuItem("Pick brunch", "ui/branch_ico.png", new GameUIItem("Something Else"));


        //nifty.showPopup(screen, popup.getId(), null);
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void toggle_console(){
        /*Element element = screen.findElementByName("console");
        element.setVisible(!element.isVisible());*/
    }

    public void toggle_inventory(){

    }
}
