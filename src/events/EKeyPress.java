/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

/**
 *
 * @author Administrator
 */
public class EKeyPress extends Event {
    public int key;
    public EKeyPress(int key){
        this.key = key;
    }
}
