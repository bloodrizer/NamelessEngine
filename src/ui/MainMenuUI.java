/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.textfield.controller.TextField;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;
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
        nifty.fromXml(
                MainMenuUI.class.getResource("main_menu_ui.xml").getPath(),
                "start"
        );
    }

    public void login(){

        TextFieldControl login = screen.findControl( "input_login",  TextFieldControl.class);
        TextFieldControl passwd = screen.findControl( "input_password",  TextFieldControl.class);

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
