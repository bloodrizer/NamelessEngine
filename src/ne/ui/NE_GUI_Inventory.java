/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import actions.IAction;
import events.EContainerUpdate;
import events.EMouseClick;
import events.Event;
import events.EventManager;
import events.IEventListener;
import items.BaseItem;
import items.BaseItemAction;
import items.ItemContainer;
import java.util.ArrayList;
import java.util.Iterator;
import ne.Game;
import ne.Input.MouseInputType;
import org.newdawn.slick.Color;
import render.overlay.OverlaySystem;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Inventory extends NE_GUI_Frame implements IEventListener{

    NE_GUI_Element inv_layer;


    public class NE_GUI_Inventory_Item extends NE_GUI_Sprite {

        BaseItem item;
        NE_GUI_Inventory_Item(BaseItem item){
            this.item = item;
            //EventManager.subscribe(this);   //<- exception there
        }
        @Override
        public void render(){
            super.render();
            OverlaySystem.ttf.drawString(
                get_x()+w-8,
                get_y()+h-15,
                Integer.toString(item.get_count()),
                Color.black
            );

            render_children();  //<-- required by a popup
        }

        @Override
        public void e_on_mouse_click(EMouseClick e){
            //System.out.println("NE_GUI_Inventory_Item::e_on_mouse_click()");
            if (e.type == MouseInputType.RCLICK){
                context_popup(e);
            }
        }

        public void context_popup(EMouseClick event){
            class ActionDrop extends BaseItemAction{
                BaseItem item;
                public ActionDrop(BaseItem item){
                    this.item = item;
                    this.name = "drop";
                }

                @Override
                public void execute() {
                    System.out.println("Item is dropped");
                    item.drop();
                }

            }

            System.out.println("Popup there");

            NE_GUI_Popup __popup = new NE_GUI_Popup();
            //NE_GUI_System ui =  Game.get_game_mode().get_ui().get_nge_ui();


            //ui.root.add(__popup);
            add(__popup);
            __popup.x = event.origin.getX();
            __popup.y = event.get_window_y();

            //__popup.x = event.origin.getX();
            //__popup.y = event.get_window_y();
            //-------------------------------------------------
            ArrayList action_list = item.get_action_list();
            Iterator<IAction> itr = action_list.iterator();

            while (itr.hasNext()){
                IAction element = itr.next();
                __popup.add_item(element);
            }

            __popup.add_item(new ActionDrop(item));
        }
    }


    public NE_GUI_Inventory(){
        super(true);

        this.set_tw(7);
        this.set_th(5);

        EventManager.subscribe(this);

        /* this is invisible container to store item icons
         * since NE_GUI_Inventory is container itself, we net
         * to separate window-related static elements
         * from dinamic icons */
        inv_layer = new NE_GUI_Element(){
            @Override
            public void e_on_mouse_click(EMouseClick e){
                System.out.println("InventoryLayer::click");
            }
        };

        inv_layer.x = 32;
        inv_layer.y = 32;
        inv_layer.w = (this.t_window_w-2)*32;
        inv_layer.h = (this.t_window_h-2)*32;


        //inv_layer.solid = false;  DO NOT USE IT
        inv_layer.dragable = false;
        
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

        inv_layer.clear();

        BaseItem[] items = (BaseItem[]) container.items.toArray(new BaseItem[0]);
        for(int i = 0; i< items.length; i++){

            BaseItem item = (BaseItem)items[i];

            NE_GUI_Inventory_Item item_control = new NE_GUI_Inventory_Item(item);
            inv_layer.add(item_control);

            item_control.sprite_name = "gfx/items/"+ item.get_type() +".png";

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
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void e_on_mouse_click(EMouseClick e){
        System.out.println("NE_GUI_Inventory::click");
    }
}
