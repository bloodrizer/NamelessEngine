/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import ne.io.Io;
import org.newdawn.slick.Color;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class VersionOverlay {
    public static void render(){
        OverlaySystem.ttf.drawString(5, WindowRender.get_window_h() - 18,
                "Ver."+Io.CLIENT_VER +
                "("+Io.PROTO_VER+")",
        Color.white);
    }
}
