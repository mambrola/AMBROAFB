/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.accounts.Account;
import ambroafb.accounts.AccountManager;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;

/**
 *
 * @author dkobuladze
 */
public class EPManagerFactory {
    
    public static EditorPanelableManager getEPManager(EditorPanelable object){
        if (object instanceof Account){
            return new AccountManager();
        }
        return null;
    }
    
}
