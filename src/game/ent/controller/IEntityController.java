/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent.controller;

import game.ent.Entity;

/**
 *
 * @author Administrator
 */
public interface IEntityController {


    public void attach(Entity entity);
    public void think();
}
