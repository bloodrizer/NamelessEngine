/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import game.ent.Entity;
import player.Player;
import world.WorldModel;

/**
 *
 * @author Administrator
 */
public class PlayerController extends NpcController{

    @Override
    public void e_on_obstacle(int x, int y) {

        if(!Player.is_combat_mode()){
            return;
        }

        Entity obstacle = WorldModel.get_tile(x, y).get_obstacle();
        if(obstacle!=null && owner.get_combat() !=null){
            owner.get_combat().inflict_damage(obstacle);
        }
    }
}
