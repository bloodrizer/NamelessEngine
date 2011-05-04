/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;




/**
 *
 * @author Administrator
 */


public class MainMenuUI implements IUserInterface, ScreenController{

    public MainMenuUI(){

    }

    public void build_ui(Nifty nifty){
        nifty.fromXml(
                MainMenuUI.class.getResource("main_menu_ui.xml").getPath(),
                "start"
        );
    }

    public void bind(Nifty nifty, Screen screen) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }



}
