/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

/**
 *
 * @author Administrator
 */
public class BaseItem{

    int count = 1;
    int max_count = 1;
    String type = "undefined";

    /*
     * Create an instance of BaseItem
     * 
     *
     */

    public static BaseItem produce(String type, int count){
        BaseItem item = new BaseItem();

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

    /*
     * Get a defensive copy of BaseItem
     */
    public BaseItem getItem() {
        BaseItem item = BaseItem.produce(type, count);
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

}