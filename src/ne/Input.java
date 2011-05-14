/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

/**
 *
 * @author Administrator
 */
import events.EKeyPress;
import events.EMouseDrag;
import events.EMouseClick;
import events.EMouseRelease;
import java.util.Collections;

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

    public static boolean key_state_alt = false;
    private static java.util.Map<Integer,Boolean> key_states
            = Collections.synchronizedMap(new java.util.HashMap<Integer,Boolean>(100));
    

    public static void update(){

        //Mouse.
        while (Mouse.next()){

            int btn = Mouse.getEventButton();

            //distance in mouse movement from the last getDX() call.
            dx = Mouse.getEventDX();
            //distance in mouse movement from the last getDY() call.
            dy = Mouse.getEventDY();

            int x = Mouse.getX();
            int y = Mouse.getY();

            /*if (Mouse.getEventButtonState()) { //button pressed
                if( Mouse.getEventButton() == 0){   //lmb
                    new EMouseClick(new Point(x,y), MouseInputType.LCLICK).post();
                    lmb_pressed = true;
                }
                if( Mouse.getEventButton() == 1){   //rmb
                    new EMouseClick(new Point(x,y), MouseInputType.RCLICK).post();
                    rmb_pressed = true;
                }
            }else{
                
            }*/

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


        //Keyboard Shit

        while (Keyboard.next()) {
	    if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_LMENU) {
		    key_state_alt = true;
		}

                Boolean state = key_states.get(Keyboard.getEventKey());
                if(state == null || state == false){
                    EKeyPress event = new EKeyPress(
                                Keyboard.getEventKey(),
                                Keyboard.getEventCharacter()
                            );
                    event.post();
                }
                key_states.put(Keyboard.getEventKey(), true);

            }else{
                if (Keyboard.getEventKey() == Keyboard.KEY_LMENU) {
		    key_state_alt = false;
		}
                key_states.put(Keyboard.getEventKey(), false);
            }
        }

        //----------------------------------------------------------------------
        
    }
}
