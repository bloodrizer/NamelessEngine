/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;

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
        this.nifty = nifty;
   
        nifty.fromXml("game_ui.xml",
                MainMenuUI.class.getResourceAsStream("game_ui.xml"),
        "start");

          /*Screen mainScreen = new ScreenBuilder("main") {{
            controller(new GameUI());
            layer(new LayerBuilder("layer") {{
                backgroundColor("#003f");
                childLayoutCenter();

                control(new ChatBuilder("chat", 14) {{
                  sendLabel("Send Message");
                }});
            }});
        }}.build(nifty);*/
    }


    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.screen = screen;
    }

    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
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
                case Keyboard.KEY_F12:
                    

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

        this.popup = nifty.createPopup("niftyPopupMenu");

        Menu<IEntityAction> popupMenu =
        popup.findNiftyControl("#menu", Menu.class);
        popupMenu.setWidth(new SizeValue("250px"));


        //-------------------------------------------------
        IEntityAction[] action_list = ent.get_action_list();

        System.out.println("Fetched "+Integer.toString(action_list.length)+"actions");
        for(int i=action_list.length-1; i>=0; i--){
            popupMenu.addMenuItem(action_list[i].get_name(), "ui/branch_ico.png", action_list[i]);
        }
        //-------------------------------------------------

         // this is required and is not happening automatically!
        //popupMenu.addMenuItem("Cut tree", "ui/axe_ico.png", new GameUIItem("Something"));
        //popupMenu.addMenuItemSeparator();
        //popupMenu.addMenuItem("Pick brunch", "ui/branch_ico.png", new GameUIItem("Something Else"));


        nifty.showPopup(screen, popup.getId(), null);
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void toggle_console(){

        //ConsoleControl console = screen.findControl( "console",  ConsoleControl.class);
        Element element = screen.findElementByName("console");
        if (!element.isVisible()){
            element.setVisible(true);
        }else{
            element.setVisible(false);
        }
        //console.
        //nifty.
        //screen.s
 
    }
}
