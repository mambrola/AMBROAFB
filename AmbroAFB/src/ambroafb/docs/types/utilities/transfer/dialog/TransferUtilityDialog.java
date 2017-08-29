/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.transfer.dialog;

import ambroafb.docs.types.utilities.transfer.TransferUtility;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DocDialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class TransferUtilityDialog extends UserInteractiveStage implements DocDialogable {

    private TransferUtility transferUtility, transferUtilityBackup;
    
    private TransferUtilityDialogController dialogController;
    
    public TransferUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner,  Names.LEVEL_FOR_PATH, "tranfer_utility_dialog_title", "/images/dialog.png");

        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/utilities/transfer/dialog/TransferUtilityDialog.fxml", null);
        dialogController = (TransferUtilityDialogController) currentScene.getProperties().get("controller");
//        dialogController.bindClient(this.transferUtility); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
//        dialogController.setNextVisibleAndActionParameters(buttonType);
//        dialogController.setBackupClient(this.transferUtilityBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return transferUtility;
    }

    public void operationCanceled(){
        transferUtility = null;
    }
}
