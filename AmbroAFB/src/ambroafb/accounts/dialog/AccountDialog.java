/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambroafb.accounts.Account;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class AccountDialog extends UserInteractiveDialogStage implements Dialogable {

    private Account account;
    private final Account accountBackup;
    
    public AccountDialog(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE buttonType, EditorPanelable object) {
        super(owner, buttonType, "/ambroafb/accounts/dialog/AccountDialog.fxml");
    
        if (object == null)
            account = new Account();
        else 
            account = (Account) object;
        accountBackup = account.cloneWithID();
        
        dialogController.setSceneData(account, accountBackup, buttonType);
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return account;
    }

    @Override
    public void operationCanceled() {
        account = null;
    }
    
    @Override
    public Account getSceneObject(){
        return account;
    }

    
}
