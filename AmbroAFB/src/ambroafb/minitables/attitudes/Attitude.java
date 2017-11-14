/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import ambroafb.minitables.MiniTable;

/**
 *
 * @author dato
 */
public class Attitude extends MiniTable {

    
    public Attitude() {
    }
    
    @Override
    public Attitude cloneWithoutID() {
        Attitude clone = new Attitude();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Attitude cloneWithID() {
        Attitude clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        Attitude otherAttitude = (Attitude) other;
        return  getRecId() == otherAttitude.getRecId() && compares(otherAttitude);
    }
}
