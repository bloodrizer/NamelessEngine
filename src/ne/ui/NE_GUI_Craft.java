/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EMouseClick;
import game.craft.CraftFormula;
import game.craft.CraftGroup;
import game.craft.CraftManager;
import game.craft.CraftRecipe;
import items.BaseItem;
import ne.Game;
import ne.Main;
import player.Player;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Craft extends NE_GUI_FrameModern {


    NE_GUI_CraftRecipes recipes_layer;


    public NE_GUI_Craft(){
        super(true);
        set_title("Craft Recipes");

        CraftManager.init();
        String[] craft_groups = CraftManager.get_groups();

        this.set_tw(5);
        this.set_th(craft_groups.length+2);

        
        for (int i = 0; i<craft_groups.length; i++){

            final int _index = i;

            NE_GUI_Button button = new NE_GUI_Button() {
                int index;
                {
                    this.index = _index;
                }

                @Override
                public void e_on_mouse_click(EMouseClick e){
                    CraftGroup group = CraftManager.groups.get(text);
                    BaseItem[] items = Player.get_ent().container.items.toArray(new BaseItem[0]);
                    
                    /*BaseItem[] items = {
                            BaseItem.produce("branch",5),
                            BaseItem.produce("stone",5)
                    };*/
                    
                    CraftFormula[] recipes = group.get_aviable_recipes(items);
                    /*for(int i = 0; i<recipes.length; i++){
                        System.out.println(recipes[i]);
                    }*/
                    if (recipes.length>0){
                        show_recipes(this, recipes);
                    }
                    
                }
            };
            add(button);
            button.y = (i+1)*(button.h+4)-4;    //lot's of aestetic magic constants
            button.x = 32;
            button.set_tw(3);
            button.text = craft_groups[i];

            button.dragable = false;
        }
        //-----------
        
        //add(recipes_layer);

    }

    public void show_recipes(NE_GUI_Button button, CraftFormula[] recipes){
        recipes_layer = new NE_GUI_CraftRecipes();
        recipes_layer.set_title(button.text);

        Game.get_game_mode().get_ui().get_nge_ui().root.add(recipes_layer);

        recipes_layer.x = button.get_x() + button.w/2;
        recipes_layer.y = button.get_y() + button.h/2;

        recipes_layer.update(recipes);
    }
}
