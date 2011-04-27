/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import ne.Input.MouseInputType;

/**
 *
 * @author Administrator
 */
public class EMouseDrag extends Event{
    public MouseInputType type = null;

    public float dx;
    public float dy;

    public EMouseDrag(float dx, float dy, MouseInputType type){
        this.dx = dx;
        this.dy = dy;
        this.type = type;
    }
}
