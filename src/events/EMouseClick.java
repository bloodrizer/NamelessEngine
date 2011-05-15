/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import ne.Input.MouseInputType;
import org.lwjgl.util.Point;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class EMouseClick extends Event {
    public Point origin = null;
    
    public MouseInputType type = null;

    public EMouseClick(Point origin, MouseInputType type){
        this.origin = origin;
        this.type = type;
    }

    public int get_window_y(){
        return WindowRender.get_window_h() - origin.getY();
    }
}
