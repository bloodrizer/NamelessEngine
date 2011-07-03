/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EGUIDrop;
import events.Event;
import events.network.EBuildStructure;
import game.ent.buildings.BuildManager;
import game.ent.buildings.EntBuilding;
import items.BaseItem;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import player.Player;
import render.WindowRender;
import world.WorldModel;
import world.WorldView;

/**
 *
 * @author Administrator
 */
public class NE_GUI_System {
    public final NE_GUI_Element root = new NE_GUI_Element(){

        {
            w = WindowRender.get_window_w();
            h = WindowRender.get_window_h();

            dragable = false;
            solid = false;
        }

        @Override
        public void e_on_grab(EGUIDrop event){
            if (event.element instanceof NE_GUI_InventoryItem){

                    BaseItem item = ((NE_GUI_InventoryItem) event.element).item;
                    System.out.println("WorldArea: grabed item " + item.get_type());
                    Class building = BuildManager.get_building(item.get_type());

                    if (building!= null){
                        //EntBuilding ent_building = (EntBuilding) building.newInstance();

                        int ex = event.coord.getX();
                        int ey = WindowRender.get_window_h()-event.coord.getY();

                        System.out.println("spawning building");

                        Point tile_coord = WorldView.getTileCoord(ex,ey);

                        if (WorldModel.tile_blocked(tile_coord)){
                            return; //do not allow to build on blocked tile
                        }

                        /*ent_building.spawn(54321, tile_coord);
                        ent_building.set_blocking(true);*/

                        EBuildStructure build_event = new EBuildStructure(tile_coord,item.get_type());
                        build_event.post();



                        Player.get_ent().container.remove_item(
                            BaseItem.produce(item.get_type(), 1)
                        );
                    }else{
                        System.err.println("item-related entity is null");
                    }
            }
        }


    }; //big invisible container

    public void render(){
        //glEnable(GL_TEXTURE_2D);
        //glDisable(GL_BLEND);

        root.render();

        //glEnable(GL_BLEND);
    }

    public void clear(){
        root.clear();
    }


    public NE_GUI_System(){

        //init root coord system
        root.x = 0;
        root.y = 0;

        //EventManager.subscribe(this);

        
    }

    public void e_on_event(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        root.notify_event(event);
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void show_message(String title, String text) {
        NE_GUI_FrameModern messagebox = new NE_GUI_FrameModern(true);
        root.add(messagebox);
        
        messagebox.set_title(title);

        messagebox.set_tw( text.length()*9/messagebox.TITLE_SIZE + 2 );
        messagebox.set_th( 2 );
        messagebox.center();


        NE_GUI_Label message = new NE_GUI_Label();
        message.set_text(text);
        messagebox.add(message);
        message.center();
        //message.y = 20;
    }
}
