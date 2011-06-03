/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.craft;

import items.BaseItem;
import java.util.ArrayList;
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

        public CraftFormula[] get_aviable_recipes(BaseItem[] items){

            ArrayList<CraftFormula> aviable_recipes = new ArrayList<CraftFormula>(64);

            CraftRecipe[][] recipes_arr = recipes.keySet().toArray(new CraftRecipe[0][0]);
            boolean recipe_invalied;

            for(int i=0; i<recipes_arr.length; i++){
                recipe_invalied = false;
                for (int j=0; j<recipes_arr[i].length; j++){
                    if (!contains(recipes_arr[i][j], items)){
                        recipe_invalied = true;
                    }
                }
                if (!recipe_invalied){
                    //System.out.println("found recipe result!: "+recipes.get(recipes_arr[i]));
                    aviable_recipes.add(
                           new CraftFormula(recipes_arr[i],recipes.get(recipes_arr[i]))
                    );
                }
            }

            return aviable_recipes.toArray(new CraftFormula[0]);
        }

        /*
         * helper function
         * Returns true, if current craft recipe element accessible with given item set
         */
        private static boolean contains(CraftRecipe recipe, BaseItem[] items){
            int count = 0;

            for (int i=0; i<items.length; i++){
                if (items[i].get_type().equals(recipe.item)){
                    count += items[i].get_count();
                }
            }

            if (count>=recipe.count){
                return true;
            }
            System.out.println(recipe+"is not contained in item set ("+ count + " < " + recipe.count + " )");

            return false;
        }
}
