/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.monsters;

import game.ent.Entity;
import game.ent.EntityNPC;
import items.BaseItem;

/**
 * Monster is a basic class for eny aggressive mob.
 * Note that it is a temporary workaround for chacking if enemy is aggressive.
 */
public class EntMonster extends EntityNPC{
    public void drop_loot(Entity killer, String item_type, int count, int rate){
        if(killer==null || killer.container == null){
            return;
        }
        int chance = (int)(Math.random()*100);

        if (chance<rate){
            killer.container.add_item(
                BaseItem.produce(item_type, count)
            );
        }
        
    }
}
