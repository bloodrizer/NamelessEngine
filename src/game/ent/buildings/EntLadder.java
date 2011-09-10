/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import java.util.ArrayList;
import player.Player;

/**
 *
 * @author Administrator
 */
public class EntLadder extends EntBuilding {
    @Override
    public ArrayList get_action_list(){
        class ActionGoDown extends BaseEntityAction{

            @Override
            public void execute() {
                if (assert_range()){
                    Player.set_zindex(Player.get_zindex()+1);
                }
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionGoDown(),"Go down");

        return list.get_action_list();
    }
}
