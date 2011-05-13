/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder.Align;
import de.lessvoid.nifty.builder.ElementBuilder.VAlign;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.controls.window.WindowControl;
import de.lessvoid.nifty.controls.window.builder.WindowBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

/**
 *
 * @author Administrator
 */

//builder helper

public class UIItemContainer {
    WindowBuilder window = null;
    WindowControl control = null;

    int CONTAINER_W = 10;
    int CONTAINER_H = 5;

    public UIItemContainer(LayerBuilder ui_layer){
        window = new WindowBuilder("containerWindow", "container");

        window.width("320px");
        window.height("200px");

        window.x("220px");
        window.y("350px");

        window.visible(false);  //hidden by default

        //window.childLayoutVertical(); <-center layout

        //PanelBuilder test = new PanelBuilder();
        //test.
        //test.childLayoutHorizontal()
        //test.color(Color.randomColor())
        //test.

        window.panel(new PanelBuilder("window_content"){{
                //height("90%");
                //width("100%");

                //valignCenter();
                //alignCenter();

                //backgroundColor(Color.randomColor());
                childLayoutVertical();

                for (int i = 0; i<CONTAINER_H; i++){
                    
                    panel(new PanelBuilder() {{
                      //height("32px");
                      childLayoutHorizontal();
                      //backgroundColor(Color.randomColor());
                      valignBottom();
                      alignLeft();

                          for(int j = 0; j<CONTAINER_W; j++){
                              control(new DroppableBuilder() {{

                                  width("32px");
                                  height("32px");

                                  //backgroundColor(Color.randomColor());
                                  childLayoutHorizontal();
                                  
                                  panel(new PanelBuilder() {{
                                      childLayoutHorizontal();
                                      image(new ImageBuilder() {{
                                        filename("ui/inv_slot.png");
                                      }});
                                  }});
                              }});
                          }
                      
                       }});
                       
                    }   //rows

              }});

            

        ui_layer.control(window);

    }

    public WindowBuilder get_builder(){
        return window;
    }

    Element window_element = null;
    public void build(Nifty nifty, Screen screen){
        window_element = window.build(nifty, screen, screen.findElementByName("game_ui_layer"));
        control = window_element.getControl(WindowControl.class);

    }

    boolean visible = false;
    public void show(){
        window_element.show();
        visible = true;
    }

    public void hide(){
        window_element.hide();
        visible = false;
    }

    public void toggle(){
        visible = !visible;
        window_element.setVisible(visible);
    }

    public void set_title(String title){
        control.setTitle(title);
    }
}
