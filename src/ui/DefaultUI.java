/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import ne.ui.NE_GUI_System;

/**
 *
 * @author Administrator
 */
public class DefaultUI implements IUserInterface {

    public void build_ui(Nifty nifty){

    }
    
    NE_GUI_System ui;
    public NE_GUI_System get_nge_ui() {
        return ui;
    }
}
