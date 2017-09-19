/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class Conversion extends EditorPanelable {

    @Override
    public Conversion cloneWithoutID() {
        Conversion clone = new Conversion();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Conversion cloneWithID() {
        Conversion clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        return false;
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
