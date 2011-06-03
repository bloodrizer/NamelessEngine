/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.craft;

/**
 *
 * @author Administrator
 */
public class CraftRecipe {
    public String item;
    public int count;

    CraftRecipe(String string, int i) {
        item = string;
        count = i;
    }

    @Override
    public String toString(){
        return "[Recipe: "+item+"("+count+")]";
    }

}
