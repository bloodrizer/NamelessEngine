/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import items.BaseItem;
import items.ItemContainer;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Inventory extends NE_GUI_Frame{
    public NE_GUI_Inventory(){
        super(true);

        this.set_tw(7);
        this.set_th(5);
    }

    ItemContainer container;
    public void set_container(ItemContainer container){
        this.container = container;

        /*children.clear();

        BaseItem[] items = (BaseItem[]) container.items.toArray(new BaseItem[0]);
        for(int i = 0; i< items.length; i++){
            
            BaseItem item = (BaseItem)items[i];

            NE_GUI_Label item_control = new NE_GUI_Label();
            add(item_control);
            item_control.text = item.get_type() 
                    + Integer.toString(item.get_count());

            item_control.x = i * 32;
            item_control.y = 0;
            item_control.w = 32;
            item_control.h = 32;
        }*/

    }

    @Override
    public void render(){
        super.render();
    }
}
