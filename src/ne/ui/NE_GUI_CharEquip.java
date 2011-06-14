/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import items.EquipContainer;
import game.ent.EntityPlayer;
import player.Player;
import render.Render;
import events.EMouseClick;
import events.EGUIDrop;
import items.BaseItem;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;


public class NE_GUI_CharEquip extends NE_GUI_FrameModern{
    public class NE_GUI_EquipSlot extends NE_GUI_Sprite {

        BaseItem item;
        public int id;
        String slot_type;

        @Override
        public void e_on_grab(EGUIDrop event){
            System.out.println("I grabbed "+event.element);
            //
            if (event.element instanceof NE_GUI_InventoryItem){
                NE_GUI_InventoryItem item_control = (NE_GUI_InventoryItem)(event.element);

                //assign_item(item_control.item);
                if(item_control.item.get_slot().equals(slot_type)){
                    assign_item(item_control.item);
                    Player.get_ent().container.remove_item(item_control.item);
                }else{
                    System.err.println("Ent type '"+ item_control.item.get_slot()+ "' not equals '"+ slot_type +"'");
                }
            }
        }

        /*
         * This copypaste is priobably not nececery
         *
         */
        public void assign_item(BaseItem item){
            this.item = item.getItem();
        }

        //allow click based slot select
        @Override
        public void e_on_mouse_click(EMouseClick e){
            NE_GUI_QuickslotBar.active_slot = id;
            //todo: meke slot select method
        }

        @Override
        public void render(){


            super.render();

            if (item == null){
                return;
            }

            //now render assigned item in the slot

            Render.bind_texture("gfx/items/"+item.get_type()+".png");

            int x = this.get_x();
            int y = this.get_y();

            glEnable(GL_TEXTURE_2D);
            glColor3f(1.0f,1.0f,1.0f);

            glBegin(GL_QUADS);
                    glTexCoord2f(0.0f, 0.0f);
                glVertex2f( x,   y);
                    glTexCoord2f(0.0f+1.0f, 0.0f);
                glVertex2f( x+w, y);
                    glTexCoord2f(0.0f+1.0f, 0.0f+1.0f);
                glVertex2f( x+w, y+h);
                    glTexCoord2f(0.0f, 0.0f+1.0f);
                glVertex2f( x,   y+h);
            glEnd();
        }


        @Override
        public String toString(){
            return "#Quickslot#";
        }
    }

    //--------------------------------------------------------------------------
    public static final int SLOT_COUNT = 4;
    
    public NE_GUI_CharEquip(){

        this.w = SLOT_COUNT*32 + (SLOT_COUNT-1)*10;
        this.h = 32;

        for (int i = 0; i<SLOT_COUNT; i++){
            NE_GUI_EquipSlot slot = new NE_GUI_EquipSlot();
            add(slot);

            //slot.slot_type = ((EntityPlayer)Player.get_ent()).equipment.slot_list[i];

            //static slot list will be enough for now
            //use npc-related slot list later
            slot.slot_type = EquipContainer.slot_list[i];

            slot.x = 20; //size+offset
            slot.y = 20 + i*(32+8);
            slot.w = 32;
            slot.h = 32;

            slot.sprite_name = "/ui/inv_slot.png";

            slot.dragable = false;

            slot.id = i;

        }
    }


}
