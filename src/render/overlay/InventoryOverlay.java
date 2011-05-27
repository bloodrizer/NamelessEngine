/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render.overlay;

import items.BaseItem;
import org.newdawn.slick.Color;
import player.Player;

/**
 *
 * @author Administrator
 */
public class InventoryOverlay {
        public static void render(){

        if (Player.get_ent() == null){
            return;
        }


        BaseItem[] items = (BaseItem[]) Player.get_ent().container.items.toArray(new BaseItem[0]);

        OverlaySystem.ttf.drawString(10, 100, "Inventory:", Color.white );

        for(int i = 0; i< items.length; i++){
            OverlaySystem.ttf.drawString(10, 120 + i*20,
                items[i].get_type()+":"+
                Integer.toString( items[i].get_count() )
            , Color.white);
        }

        

    }
}
