/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class BalanceAccountDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private BalanceAccount balAccount;
    private final BalanceAccount balAccountBackup;
    
    public BalanceAccountDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, buttonType, "/ambroafb/balance_accounts/dialog/BalanceAccountDialog.fxml");
        
        if (object == null)
            balAccount = new BalanceAccount();
        else
            balAccount = (BalanceAccount) object;
        balAccountBackup = balAccount.cloneWithID();
        
        dialogController.setSceneData(balAccount, balAccountBackup, buttonType);
    }

    @Override
    public BalanceAccount getResult() {
        showAndWait();
        return balAccount;
    }

    @Override
    public void operationCanceled() {
        balAccount = null;
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return balAccount;
    }

    
}
