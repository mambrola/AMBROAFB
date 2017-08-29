/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.transfer;

import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class TransferUtility extends EditorPanelable {


    @Override
    public TransferUtility cloneWithoutID() {
        return null;
    }

    @Override
    public TransferUtility cloneWithID() {
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
