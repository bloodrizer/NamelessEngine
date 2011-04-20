/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class Entity {

    public Point origin;
    private int uid = 0;

    public void spawn(int uid, Point origin){
        this.uid = uid;
        this.origin = origin;
    }
}
