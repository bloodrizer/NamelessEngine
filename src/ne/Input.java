/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

/**
 *
 * @author Administrator
 */
import events.EMouseDrag;
import events.EMouseClick;
import events.EMouseRelease;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class Input {

    public static float dx = 0;
    public static float dy = 0;

    private static boolean lmb_pressed = false;
    private static boolean rmb_pressed = false;

    public enum MouseInputType {
        LCLICK,
        RCLICK
    };

    public static void update(){

        //Mouse.


        //distance in mouse movement from the last getDX() call.
        dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        dy = Mouse.getDY();

        //System.out.println(Float.toString(dx)+","+Float.toString(dy));

        //--------------------------------LMB-----------------------------------
        if (Mouse.isButtonDown(0)){
            if (!lmb_pressed){
                lmb_pressed = true;
                new EMouseClick(new Point(Mouse.getX(),Mouse.getY()), MouseInputType.LCLICK).post();
            }else{  //mouse was pressed on previouse polling, so it should be draging
                 new EMouseDrag(dx, dy, MouseInputType.LCLICK).post();
            }
           
        }else{
            if (lmb_pressed){
                 new EMouseRelease( MouseInputType.LCLICK).post();
            }
            lmb_pressed = false;
        }
        //--------------------------------RMB-----------------------------------
        if (Mouse.isButtonDown(1)){
            if (!rmb_pressed){
                rmb_pressed = true;
                new EMouseClick(new Point(Mouse.getX(),Mouse.getY()), MouseInputType.RCLICK).post();
            }else{  //mouse was pressed on previouse polling, so it should be draging
                 Mouse.setGrabbed(true);
                 new EMouseDrag(dx, dy, MouseInputType.RCLICK).post();
            }
        }else{
            if (rmb_pressed){
                Mouse.setGrabbed(false);
                new EMouseRelease( MouseInputType.RCLICK).post();
            }
            rmb_pressed = false;
        }

        
    }
}
