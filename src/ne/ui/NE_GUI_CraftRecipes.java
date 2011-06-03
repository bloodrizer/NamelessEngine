/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import game.craft.CraftFormula;

/**
 * This gui control represents a layer of recipes for set subcategory
 * of craft menu
 *
 */
public class NE_GUI_CraftRecipes extends NE_GUI_Frame {

    public NE_GUI_CraftRecipes(){
        super(true);
        this.set_tw(5);
        this.set_th(8);
    }

    public void update(CraftFormula[] formulas){
        clear();

        for(int i = 0; i< formulas.length; i++){
            //formulas[i];
        }
    }

}
