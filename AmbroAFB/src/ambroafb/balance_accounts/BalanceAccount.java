/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;

/**
 *
 * @author dato
 */
public class BalanceAccount extends EditorPanelable {

    public BalanceAccount(){
        
    }
    
    static ArrayList<BalanceAccount> getAllFromDB() {
        return new ArrayList<>();
    }

    @Override
    public BalanceAccount cloneWithoutID() {
        BalanceAccount clone = new BalanceAccount();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public BalanceAccount cloneWithID() {
        BalanceAccount clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        //...
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
