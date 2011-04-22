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
public class BaseController implements IEntityController {

    protected Entity owner = null;

    public void attach(Entity entity){
        this.owner = entity;
    }

    public void think(){

    }
}
