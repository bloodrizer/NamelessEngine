/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EMouseClick;
import game.craft.CraftFormula;
import items.BaseItem;
import player.Player;

/**
 * This gui control represents a layer of recipes for set subcategory
 * of craft menu
 */
public class NE_GUI_CraftRecipes extends NE_GUI_Frame {

    public NE_GUI_CraftRecipes(){
        super(true);
        this.set_tw(5);
        this.set_th(8);
    }

    public void update(CraftFormula[] formulas){
        //clear();

        this.set_th(2+formulas.length);

        for(int i = 0; i< formulas.length; i++){
            final CraftFormula __formula = formulas[i];

            NE_GUI_Button button = new NE_GUI_Button() {
                CraftFormula formula;
                {
                    this.formula = __formula;
                }
                @Override
                public void e_on_mouse_click(EMouseClick e){

                    //remove craft ingridients
                    for(int i = 0; i<formula.recipes.length; i++){
                        Player.get_ent().container.remove_item(
                            BaseItem.produce(
                                formula.recipes[i].item,
                                formula.recipes[i].count
                            )
                        );
                    }

                    /*
                     * TODO: by default you recieving 1 item of
                     * craft result.
                     * Introduce craft result count if nececery
                     */

                    Player.get_ent().container.add_item(
                            BaseItem.produce(formula.result, 1)
                    );

                    //close this screen
                    parent.visible = false;
                }
            };

            add(button);
            button.y = (i+1)*(button.h+4)-4;    //lot's of aestetic magic constants
            button.x = 32;
            button.set_tw(3);
            button.text = formulas[i].result;

            button.dragable = false;

        }
    }

}
