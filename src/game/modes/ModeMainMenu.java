/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

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

    public IUserInterface get_ui(){
        IUserInterface wgt = new MainMenuUI();

        return wgt;
    }
}
