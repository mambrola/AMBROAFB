/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class ChargeUtility extends EditorPanelable {

    @Override
    public EditorPanelable cloneWithoutID() {
        return null;
    }

    @Override
    public EditorPanelable cloneWithID() {
        return null;
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
