/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import client.ClientGameEnvironment;
import game.ent.Entity;
import player.Player;
import world.layers.WorldLayer;
import world.WorldModel;

/**
 *
 * @author Administrator
 */
public class MobController extends NpcController{

    @Override
    public void e_on_obstacle(int x, int y) {
        Entity actor = ClientGameEnvironment.getWorldLayer(Player.get_zindex()).get_tile(x, y).get_actor();
        if(actor!=null && owner.get_combat() !=null){
            owner.get_combat().inflict_damage(actor);
        }
    }
}
