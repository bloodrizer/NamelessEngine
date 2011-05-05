/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.lessvoid.nifty.Nifty;
import de.matthiasmann.twl.Widget;
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
