/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.accounts.dialog.AccountDialog;
import ambroafb.accounts.filter.AccountFilter;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class AccountManager extends EditorPanelableManager {

    public AccountManager(){
        dataProvider = new AccountDataProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        AccountDialog dialog = new AccountDialog(object, type, owner);
        dialog.setDataProvider(dataProvider);
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new AccountFilter(owner);
    }
    
}
