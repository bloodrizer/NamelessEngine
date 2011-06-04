/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class BaseItem{

    int count = 1;
    int max_count = 64;
    String type = "undefined";

    /*
     * Create an instance of BaseItem
     * 
     *
     */

    protected ItemContainer container;

    public void set_container(ItemContainer container){
        this.container = container;
    }
    //debug only, not safe
    public ItemContainer get_container(){
        return container;
    }

    public static BaseItem produce(String type, int count){
        BaseItem item = new BaseItem();

        //TODO: set max count there based on item type

        item.set_type(type);
        item.set_count(count);
        

        return item;
    }



    public void set_type(String type){
        this.type = type;
    }

    public String get_type(){
        return type;
    }

    public int get_count() {
        return count;
    }

    public void set_count(int count) {
        if (count>max_count){
            count = max_count;
        }
        this.count = count;
    }

    public int get_space() {
        return max_count - count;
    }

    public int get_max_count() {
        return max_count;
    }

    public boolean has_space(int insertion) {
        return (get_space() > 0);
    }

    public void add_count(int count) {
        set_count(this.count + count);
    }
    public void del_count(int count) {
        set_count(this.count - count);

        if(this.count <= 0){
            drop();
        }
    }

    /*
     * Get a defensive copy of BaseItem
     */
    public BaseItem getItem() {
        BaseItem item = BaseItem.produce(type, count);

        //should be set too to avoid bugs
        item.set_container(container);
        return item;
    }

    /*
     * Put as much possible from src entity
     * to this entity to make stack full
     */

    public void put_from(BaseItem src) {

        int to_remove = get_space();
        if ( to_remove > src.get_count() ){
            to_remove = src.get_count();
        }

        System.out.println("to remove:" + Integer.toString(to_remove));
        System.out.println("removing from src:" + Integer.toString(src.count));

        src.set_count( src.get_count() - to_remove );

        System.out.println("and now it is:" + Integer.toString(src.count));
        this.add_count(to_remove);
    }

    public boolean is_empty() {
        return (count <= 0);
    }

    public ArrayList get_action_list() {
        return new ArrayList<BaseItem>(0);
    }

    public void drop() {
        if(container==null){
            System.err.println("Failed to drop item, no container");
            return;
        }
        container.remove_item(this);
    }

}
