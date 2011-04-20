/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.matthiasmann.twl.Widget;

/**
 *
 * @author Administrator
 */
public interface IGameMode {
    public void run();
    public void update();
    
    public Widget get_ui();
}
