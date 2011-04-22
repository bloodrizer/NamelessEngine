/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import game.ent.Entity;
import game.ent.controller.NpcController;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class Player {

    static Entity player_ent = null;
    public static void set_ent(Entity ent){
        player_ent = ent;
    }

    public static void move(Point dest){
        if (player_ent != null){
            //player_ent.move_to(dest);
           ((NpcController) player_ent.controller).set_destination(dest);
        }
    }

}
