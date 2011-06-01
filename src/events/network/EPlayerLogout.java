/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import player.Player;

/**
 *
 * @author Administrator
 */
public class EPlayerLogout extends NetworkEvent {
    @Override
    public String get_id(){
        return "0x0013";
    }

    @Override
    public String[] serialize(){
        return new String[] {
            get_id(),
            Integer.toString(
                    Player.character_id
            )
        };
    }
}
