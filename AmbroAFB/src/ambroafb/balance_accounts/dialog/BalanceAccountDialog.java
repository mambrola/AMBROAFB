/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.save_button.StageUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BalanceAccountDialog extends Stage implements Dialogable {
    
    private BalanceAccount balAccount;
    private final BalanceAccount balAccountBackup;
    
    private BalanceAccountDialogController dialogController;
    
    public BalanceAccountDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        
        BalanceAccount balAccountObject;
        if (object == null)
            balAccountObject = new BalanceAccount();
        else
            balAccountObject = (BalanceAccount) object;
        
        this.balAccount = balAccountObject;
        this.balAccountBackup = balAccountObject.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/balance_accounts/dialog/BalanceAccountDialog.fxml", null);
        dialogController = (BalanceAccountDialogController) currentScene.getProperties().get("controller");
        dialogController.bindBalAccount(this.balAccount);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupBalAccount(this.balAccountBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("balaccount_dialog_title"));
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
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
