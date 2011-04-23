/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class EPlayerLogon extends NetworkEvent{
    public Point origin = null;
    public EPlayerLogon(Point origin){
        this.origin = origin;
    }
}
