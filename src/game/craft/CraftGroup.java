/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.craft;

import items.BaseItem;
import java.util.Collections;

/**
 *  This is named container for craft recipes
 */
public class CraftGroup {
        private java.util.Map<CraftRecipe[],String> recipes =
            Collections.synchronizedMap(new java.util.HashMap<CraftRecipe[],String>(6));

        public void add_recipe(CraftRecipe[] items, String result){
            recipes.put(items, result);
        }

        public CraftRecipe[] get_aviable_recipes(BaseItem[] items){


            return null;
        }
}
