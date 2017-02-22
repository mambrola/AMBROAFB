/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BalanceAccountDialog extends UserInteractiveStage implements Dialogable {
    
    private BalanceAccount balAccount;
    private final BalanceAccount balAccountBackup;
    
    private BalanceAccountDialogController dialogController;
    
    public BalanceAccountDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "balaccount_dialog_title", "/images/dialog.png");
        
        BalanceAccount balAccountObject;
        if (object == null)
            balAccountObject = new BalanceAccount();
        else
            balAccountObject = (BalanceAccount) object;
        
        this.balAccount = balAccountObject;
        this.balAccountBackup = balAccountObject.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/balance_accounts/dialog/BalanceAccountDialog.fxml", null);
        dialogController = (BalanceAccountDialogController) currentScene.getProperties().get("controller");
        dialogController.bindBalAccount(this.balAccount);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupBalAccount(this.balAccountBackup);
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
}
