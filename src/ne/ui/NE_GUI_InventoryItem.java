/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import actions.IAction;
import events.*;
import items.BaseItem;
import items.BaseItemAction;
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
public class NE_GUI_InventoryItem extends NE_GUI_Sprite {

        /*
         * Quickslot pointer is required to check if item was assigned
         * to the other quickslot
         */
    
        //NE_GUI_Quickslot quickslot;

        BaseItem item;
        NE_GUI_InventoryItem(BaseItem item){
            this.item = item;
            //EventManager.subscribe(this);   //<- exception there
        }
        @Override
        public void render(){
            super.render();

            int xoffset = 8;
            if (item.get_count()>=10){
                xoffset = 16;
            }

            OverlaySystem.ttf.drawString(
                get_x()+w-xoffset,
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

        @Override
        public void e_on_drop(){
            //update container, lol
            NE_GUI_Element __parent = parent.parent;    //lol, hacky shit
            //there we getting wrapper layer for inventory item
            //and getting parent of this wrapper


            NE_GUI_Inventory inventory = (NE_GUI_Inventory) __parent;
            inventory.update(inventory.container);
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
                    if(item!=null){
                        item.drop();
                    }
                }

            }

            System.out.println("Popup there");

            NE_GUI_Popup __popup = new NE_GUI_Popup();
            NE_GUI_System ui =  Game.get_game_mode().get_ui().get_nge_ui();


            ui.root.add(__popup);
            //add(__popup);
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