/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.build;

import game.ent.buildings.*;
import java.util.Collections;

/**
 *
 * @author Administrator
 */
public class BuildManager {
    public static java.util.Map<String,Class> links =
            Collections.synchronizedMap(new java.util.HashMap<String,Class>(128));

    static {
        init();
    }


    public static boolean is_empty(){
        return links.isEmpty();
    }

    public static void init(){
        if (!is_empty()){
            return;
        }
        links.put("fire", EntFire.class);
        links.put("fence", EntFence.class);
    }

    public static Class get_building(String _type) {
        return links.get(_type);
    }

}
