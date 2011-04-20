/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;

/**
 *
 * @author Administrator
 */


public class MainMenuUI extends Widget implements IUserInterface{

    private Button button;
    public MainMenuUI(){

        button = new Button("test button");
        button.setPosition(100, 100);
        button.setSize(100, 33);
        button.setTheme("button");
        add(button);

        /*ResizableFrame frame = new ResizableFrame();
        frame.setTitle("Loging in");
        frame.setTheme("resizableframe-title");
        frame.setPosition(50,50);
        frame.setSize(500, 100);
        add(frame);*/

        EditField foo = new EditField();
        
        foo.setPosition(10, 10);
        foo.setSize(120, 20);
        foo.setTheme("editfield");
        //setPasswordMasking(true)
        add(foo);
    }

}
