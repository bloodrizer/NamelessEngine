/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import game.ent.Entity;

/**
 *
 * @author Administrator
 */
public class Player {

    static Entity player_ent = null;
    public static void set_ent(Entity ent){
        player_ent = ent;
    }

}
