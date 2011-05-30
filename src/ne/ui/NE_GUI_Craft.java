/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EMouseClick;
import game.craft.CraftManager;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Craft extends NE_GUI_Frame {


    public NE_GUI_Craft(){
        super(true);

        CraftManager.init();
        

        this.set_tw(5);
        this.set_th(8);

        String[] craft_groups = CraftManager.get_groups();
        for (int i = 0; i<craft_groups.length; i++){

            final int _index = i;

            NE_GUI_Button button = new NE_GUI_Button() {
                int index;
                {
                    this.index = _index;
                }

                @Override
                public void e_on_mouse_click(EMouseClick e){
                    //NE_GUI_Craft()
                }
            };
            add(button);
            button.y = (i+1)*(button.h+4)-4;    //lot's of aestetic magic constants
            button.x = 32;
            button.set_tw(3);
            button.text = craft_groups[i];

            button.dragable = false;
        }
    }
}
