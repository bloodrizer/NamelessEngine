/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import org.newdawn.slick.Color;
import render.overlay.OverlaySystem;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Label extends NE_GUI_Element {
    public String text = "Login";

    @Override
    public void render(){
        OverlaySystem.ttf.drawString(
                x + parent.x,
                y + parent.y,
                text , Color.black);

    }
}
