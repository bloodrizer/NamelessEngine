/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import items.ItemContainer;

/**
 *
 * @author Administrator
 */
public class EContainerUpdate extends Event{
    ItemContainer container;
    public EContainerUpdate(ItemContainer container){
        this.container = container;
    }
}
