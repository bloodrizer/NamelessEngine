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
public class EMouseRelease extends Event{
    public MouseInputType type = null;

    public EMouseRelease(MouseInputType type){
        this.type = type;
    }
}
