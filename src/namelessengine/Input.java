/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package namelessengine;

/**
 *
 * @author Administrator
 */
import events.EMouseInput;
import events.EMouseInput.MouseInputType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class Input {

    public static float dx = 0;
    public static float dy = 0;

    private static boolean lmb_pressed = false;
    private static boolean rmb_pressed = false;

    public static void update(){
        //distance in mouse movement from the last getDX() call.
        dx = Mouse.getDX();
        //distance in mouse movement from the last getDY() call.
        dy = -Mouse.getDY();

        //--------------------------------LMB-----------------------------------
        if (Mouse.isButtonDown(0)){
            if (!lmb_pressed){
                lmb_pressed = true;
                 EMouseInput event = new EMouseInput(new Point(Mouse.getX(),Mouse.getY()), MouseInputType.LCLICK);
                 event.post();
            }
           
        }else{
            lmb_pressed = false;
        }
        //--------------------------------RMB-----------------------------------
        if (Mouse.isButtonDown(1)){
            if (!rmb_pressed){
                rmb_pressed = true;
                 EMouseInput event = new EMouseInput(new Point(Mouse.getX(),Mouse.getY()), MouseInputType.LCLICK);
                 event.post();
            }
        }else{
            rmb_pressed = false;
        }
    }
}
