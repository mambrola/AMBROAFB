/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BalanceAccountDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private BalanceAccount balAccount;
    private final BalanceAccount balAccountBackup;
    
    private DialogController dialogController;
    
    public BalanceAccountDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "balaccount_dialog_title");
        
        BalanceAccount balAccountObject;
        if (object == null)
            balAccountObject = new BalanceAccount();
        else
            balAccountObject = (BalanceAccount) object;
        
        this.balAccount = balAccountObject;
        this.balAccountBackup = balAccountObject.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/balance_accounts/dialog/BalanceAccountDialog.fxml", null);
        dialogController = (BalanceAccountDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(balAccount, balAccountBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
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
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
}
