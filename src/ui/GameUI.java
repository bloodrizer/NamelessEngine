/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Menu;
/*import de.lessvoid.nifty.controls.console.controller.ConsoleControl;*/
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import events.EKeyPress;
import events.Event;
import events.EventManager;
import events.IEventListener;
import org.lwjgl.input.Keyboard;


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
         /*nifty.fromXml(
                MainMenuUI.class.getResource("game_ui.xml").getPath(),
                "start"
        );*/
        nifty.fromXml("game_ui.xml",
                MainMenuUI.class.getResourceAsStream("game_ui.xml"),
        "start");
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
                    this.popup = nifty.createPopup("niftyPopupMenu");

                    Menu<GameUIItem> popupMenu =
                            popup.findNiftyControl("#menu", Menu.class);
                    popupMenu.setWidth(new SizeValue("250px")); // this is required and is not happening automatically!
                    popupMenu.addMenuItem("MenuItem 1", "menu/listen.png", new GameUIItem("Something"));
                    popupMenu.addMenuItemSeparator();
                    popupMenu.addMenuItem("MenuItem 2", "menu/something.png", new GameUIItem("Something Else"));

                    nifty.showPopup(screen, popup.getId(), null);

                break;
            }
        }
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
