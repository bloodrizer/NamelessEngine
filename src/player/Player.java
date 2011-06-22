/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package player;

import events.network.EEntitySetPath;
import game.ent.Entity;
import game.ent.EntityPlayer;
import game.ent.controller.NpcController;
import items.BaseItem;
import ne.Input;
import ne.ui.NE_GUI_CharScreen;
import org.lwjgl.util.Point;
import ui.GameUI;

/**
 *
 * @author Administrator
 */
public class Player {

    static final int range = 2; //2 tiles

    static Entity player_ent = null;
    public static int character_id;
    public static void set_ent(Entity ent){
        player_ent = ent;
        ent.set_uid(character_id);  //special hack to synchronize client-side entity_id and server_side entity_id

        GameUI.inventory.set_container(player_ent.container);

        NE_GUI_CharScreen.update_stats();
    }

    /*
     * Set player destination to specified point
     * This method does not move player ent, it only sets position for entity controller
     */

    public static void move(Point dest){
        if (player_ent != null){
            //player_ent.move_to(dest);
           ((NpcController) player_ent.controller).set_destination(dest);
        }
    }

    public static Point get_origin(){
        return new Point(player_ent.origin);
    }

    public static Entity get_ent(){
        return player_ent;
    }
    //helper function to avoid excessive typecasting
    public static EntityPlayer get_player_ent(){
        return (EntityPlayer)player_ent;
    }

    /*
     * Return true if current entity is in range of player action
     *
     */
    public static boolean in_range(Entity ent){
        int dx = player_ent.origin.getX() - ent.origin.getX();
        int dy = player_ent.origin.getY() - ent.origin.getY();

        //System.out.println("range:" + (dx*dx + dy*dy));

        if ( range*range >= dx*dx + dy*dy ){
            return true;
        }

        return false;
    }

    public static void attack(Entity ent) {
        if (ent == null){
            System.err.println("Trying to attack null entity");
            return;
        }

        if (in_range(ent)){
            player_ent.get_combat().inflict_damage(ent);
        }else{
            move(ent.origin);
        }
    }

    public static boolean is_combat_mode(){

        //todo: check various conditions and modes there

        if (Input.key_state_ctrl){
            return true;
        }
        return false;
    }
}
