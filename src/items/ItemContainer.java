/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

import events.EContainerUpdate;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Administrator
 */
public class ItemContainer {

     //max_item slots
     public int max_items = 10;

     public Collection<BaseItem> items = new ArrayList<BaseItem>();

     public void set_max_items(int count){
         max_items = count;
     }

     /*
      * Add item stack to inventory
      *
      */

     public void add_item(BaseItem item){
        //int count = item.get_count();

        Object[] elem =  items.toArray();
        for(int i = 0; i<elem.length; i++){
            BaseItem tgt = (BaseItem)elem[i];
            if ( tgt.get_type().equals(item.get_type()) ){  //compair types
                tgt.put_from(item);
            }
            
            if (item.is_empty()){
                on_update();
                return;
            }
        }

        //no similar item found, or every stack is full -  adding new stack
        if(!is_full()){
            items.add(item.getItem());
            on_update();
        }
     }

     public boolean is_full(){
         return ( items.size() >= max_items );
     }

     public void remove_item(BaseItem item){
         int count = item.get_count();
     }

     public void on_update(){
         EContainerUpdate event = new EContainerUpdate(this);
         event.post();
     }
}
