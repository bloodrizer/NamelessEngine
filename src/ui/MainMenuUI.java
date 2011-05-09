/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
/*import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.controller.TextField;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;*/import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import ne.Game.GameModes;
import ne.Main;




/**
 *
 * @author Administrator
 */


public class MainMenuUI implements IUserInterface, ScreenController{

    public MainMenuUI(){

    }

    private static Screen screen = null;

    public void build_ui(Nifty nifty){
        nifty.fromXml("main_menu_ui.xml",
                MainMenuUI.class.getResourceAsStream("main_menu_ui.xml"),
                "start");
        
      

    }

    public void login(){

        TextField login = screen.findNiftyControl( "input_login",  TextField.class);
        TextField passwd = screen.findNiftyControl( "input_password",  TextField.class);

        String str_login = login.getText();
        String str_passwd = passwd.getText();

        Main.game.set_state(GameModes.InGame);

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



}
