/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;

/**
 *
 * @author Administrator
 */
public interface IUserInterface {
    public boolean quit = false;
    public void build_ui(Nifty nifty);
}
