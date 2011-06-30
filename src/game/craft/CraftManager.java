/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.craft;

import java.util.Collections;

/**
 *
 * @author Administrator
 */
public class CraftManager {
    public static java.util.Map<String,CraftGroup> groups =
            Collections.synchronizedMap(new java.util.HashMap<String,CraftGroup>(6));

    public static boolean is_empty(){
        return groups.isEmpty();
    }

    public static void init(){
        if (!is_empty()){
            return;
        }

        CraftGroup tools_groop = new CraftGroup();
        groups.put("Tools", tools_groop);

        tools_groop.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("branch",1),
            new CraftRecipe("stone", 1)
        }, "stone_axe");


        /*CraftGroup armor_groop = new CraftGroup();
        groups.put("Armor", armor_groop);

        CraftGroup weapon_groop = new CraftGroup();
        groups.put("Weapon", armor_groop);*/

        CraftGroup building_groop = new CraftGroup();
        groups.put("Buildings", building_groop);

        building_groop.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("branch",5)
        }, "fire");

        building_groop.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("wood_block",5)
        }, "fence");

        building_groop.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("wood_block",25)
        }, "wood_wall");

        building_groop.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("wood_block",5)
        }, "wood_floor");

        CraftGroup potions_group = new CraftGroup();
        groups.put("Potions", potions_group);

        potions_group.add_recipe(
        new CraftRecipe[] {
            new CraftRecipe("flower",5)
        }, "flower_powder");
    }

    public static String[] get_groups(){
        return (String[]) groups.keySet().toArray(new String[0]);
    }
}
