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
public class PlayerController extends NpcController{

    public PlayerController(){
        super();

        //MOVE_SPEED = 0.5f;
    }

    @Override
    public void e_on_obstacle(int x, int y) {

        if(!Player.is_combat_mode()){
            return;
        }

        Entity obstacle = ClientGameEnvironment.getWorldLayer(Player.get_zindex()).get_tile(x, y).get_obstacle();
        if(obstacle!=null && owner.get_combat() !=null){
            owner.get_combat().inflict_damage(obstacle);
        }
    }
}
