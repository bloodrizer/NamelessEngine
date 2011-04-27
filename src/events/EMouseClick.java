/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import namelessengine.Input.MouseInputType;
import org.lwjgl.util.Point;

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
}
