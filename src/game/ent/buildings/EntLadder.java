/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.buildings;

import game.ent.BaseEntityAction;
import game.ent.EntActionList;
import game.ent.EntityManager;
import java.util.ArrayList;
import player.Player;
import world.WorldModel;
import world.WorldView;
import world.layers.WorldLayer;

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

                    int layerFromID = Player.get_zindex();
                    Player.set_zindex(layerFromID+1);

                    env.getEntityManager().remove_entity(Player.get_ent(), layerFromID);
                    env.getEntityManager().add(Player.get_ent(), Player.get_zindex());

                    WorldView.set_zindex(Player.get_zindex());

                    //WorldLayer layer = WorldModel.getWorldLayer(Player.get_zindex());
                }
            }

        }


        EntActionList list = new EntActionList();
        list.set_owner(this);
        list.add_action(new ActionGoDown(),"Go down");

        return list.get_action_list();
    }
}
