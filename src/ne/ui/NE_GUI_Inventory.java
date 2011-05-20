/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EContainerUpdate;
import events.Event;
import events.EventManager;
import events.IEventListener;
import items.BaseItem;
import items.ItemContainer;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Inventory extends NE_GUI_Frame implements IEventListener{

    NE_GUI_Element inv_layer;

    public NE_GUI_Inventory(){
        super(true);

        this.set_tw(7);
        this.set_th(5);

        EventManager.subscribe(this);

        /* this is invisible container to store item icons
         * since NE_GUI_Inventory is container itself, we net
         * to separate window-related static elements
         * from dinamic icons */
        inv_layer = new NE_GUI_Element();

        inv_layer.x = 32;
        inv_layer.y = 32;
        inv_layer.solid = false;
        
        add(inv_layer);
    }

    ItemContainer container;
    public void set_container(ItemContainer container){
        this.container = container;

    }

    //returns item tile x position, based on the size of the frame container
    private int get_item_x(int i){
        return i % (this.t_window_w-2);
    }
     //returns item tile y position, based on the size of the frame container
    private int get_item_y(int i){
        return i / (this.t_window_w-2);
    }

    public void update(ItemContainer container){
        if (container != this.container){
            return;
        }

        inv_layer.children.clear();

        BaseItem[] items = (BaseItem[]) container.items.toArray(new BaseItem[0]);
        for(int i = 0; i< items.length; i++){

            BaseItem item = (BaseItem)items[i];

            NE_GUI_Label item_control = new NE_GUI_Label();
            inv_layer.add(item_control);
            /*item_control.text = item.get_type()
                    + Integer.toString(item.get_count());*/
            item_control.text = "i("+item.get_count()+")";

            item_control.x = get_item_x(i) * 32;
            item_control.y = get_item_y(i) * 32;



            item_control.w = 32;
            item_control.h = 32;
        }

    }

    @Override
    public void render(){
        super.render();
    }

    public void e_on_event(Event event) {
        if (event instanceof EContainerUpdate){
            update( ((EContainerUpdate) event).container);
        }
    }

    public void e_on_event_rollback(Event event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
