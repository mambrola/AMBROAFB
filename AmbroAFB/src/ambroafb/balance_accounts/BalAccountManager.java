/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.balance_accounts.dialog.BalanceAccountDialog;
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
public class BalAccountManager extends EditorPanelableManager {

    public BalAccountManager(){
        dataFetchProvider = new BalAccountDataFetchProvider();
        dataChangeProvider = new BalAccountDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        BalanceAccountDialog dialog = new BalanceAccountDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "balaccount_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
