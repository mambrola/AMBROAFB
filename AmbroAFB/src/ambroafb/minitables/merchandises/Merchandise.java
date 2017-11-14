/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.minitables.MiniTable;

/**
 *
 * @author dato
 */
public class Merchandise extends MiniTable {
    
    public Merchandise() {
    }
    
    @Override
    public Merchandise cloneWithoutID() {
        Merchandise clone = new Merchandise();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Merchandise cloneWithID() {
        Merchandise clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
}
