/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.craft;

/**
 *
 * @author Administrator
 */
public class CraftFormula {
    public CraftRecipe[] recipes;
    public String result;

    public CraftFormula(CraftRecipe[] recipes, String result){
        this.recipes = recipes;
        this.result = result;
    }

    @Override
    public String toString(){
        String serialized = "[CraftFormula("+result+") - ";

        for (int i =0; i< recipes.length; i++){
            serialized += recipes[i];
        }

        serialized += "]";

        return serialized;
    }
}
