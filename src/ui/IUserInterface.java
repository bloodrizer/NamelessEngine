/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import ne.ui.NE_GUI_System;

/**
 *
 * @author Administrator
 */
public interface IUserInterface {
    public boolean quit = false;
    public void build_ui();

    public NE_GUI_System get_nge_ui();
    
    public void update();
    public void render();
}
