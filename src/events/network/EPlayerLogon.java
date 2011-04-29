/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.network.NetworkEvent;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
@NetID(id=2)
public class EPlayerLogon extends NetworkEvent{
    public Point origin = null;
    public EPlayerLogon(Point origin){
        this.origin = origin;
    }
}
