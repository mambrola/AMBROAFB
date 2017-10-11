/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambroafb.accounts.Account;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class AccountDialog extends UserInteractiveDialogStage implements Dialogable {

    private Account account, accountBackup;
    
    private AccountDialogController dialogController;
    
    public AccountDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "account_dialog_title");
    
        if (object == null)
            account = new Account();
        else 
            account = (Account) object;
        accountBackup = account.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/accounts/dialog/AccountDialog.fxml", null);
        dialogController = (AccountDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(object, accountBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
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
    
    // შევიდეს Dialogable-ში.
    public boolean anyComponentChanged(){
        return dialogController.anySceneComponentChanged();
    }
    
//    private boolean permissionToClose = true;
//    
//    // შევიდეს Dialogable-ში.
//    public void changePermissionForClose(boolean value){
//        permissionToClose = value;
//    }
//    
//    // შევიდეს Dialogable-ში.
//    public boolean getPermissionToClose(){
//        return permissionToClose;
//    }
}
