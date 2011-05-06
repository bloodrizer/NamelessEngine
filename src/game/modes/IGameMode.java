/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import ui.IUserInterface;

/**
 *
 * @author Administrator
 */
public interface IGameMode {
    public void run();
    public void update();
    
    public IUserInterface get_ui();
}
