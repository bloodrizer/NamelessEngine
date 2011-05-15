/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import items.ItemContainer;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Inventory extends NE_GUI_Frame{
    public NE_GUI_Inventory(){
        super(true);
    }

    ItemContainer container;
    public void set_container(ItemContainer container){
        this.container = container;
    }
}
