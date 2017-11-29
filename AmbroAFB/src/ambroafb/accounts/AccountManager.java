/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.accounts.dialog.AccountDialog;
import ambroafb.accounts.filter.AccountFilter;
import ambroafb.general.editor_panel.EditorPanel;
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

    private final AccountDataFetchProvider accountDataFetchProvider;
    
    public AccountManager(){
        accountDataFetchProvider = new AccountDataFetchProvider();
        dataFetchProvider = new AccountDecoratorDataFetchProvider(accountDataFetchProvider);
        dataChangeProvider = new AccountDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        AccountDialog dialog = new AccountDialog(owner, type, object);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setDataFetchProvider(accountDataFetchProvider);
        dialog.setFrameFeatures(type, "account_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new AccountFilter(owner);
    }
    
    public AccountDataFetchProvider getAccountDataFetchProvider(){
        return accountDataFetchProvider;
    }
}
