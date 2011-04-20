/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Widget;
import ui.IUserInterface;
import ui.MainMenuUI;

/**
 *
 * @author Administrator
 */
public class ModeMainMenu implements IGameMode {
    public void run(){

    }

    public void update(){

    }

    public Widget get_ui(){
        Widget wgt = new MainMenuUI();

        return wgt;
    }
}
