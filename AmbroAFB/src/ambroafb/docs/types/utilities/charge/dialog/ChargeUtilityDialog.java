/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge.dialog;

import ambroafb.docs.types.utilities.charge.ChargeUtility;
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
 * @author dkobuladze
 */
public class ChargeUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private ChargeUtility chargeUtility, chargeUtilityBackup;
    
    private DialogController dialogController;
    
    public ChargeUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "doc_charge_utility_dialog_title");
        
        if (object == null){
            chargeUtility = new ChargeUtility();
        }
        else {
            chargeUtility = (ChargeUtility) object;
        }
        chargeUtilityBackup = chargeUtility.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/utilities/charge/dialog/ChargeUtilityDialog.fxml", null);
        dialogController = (ChargeUtilityDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(chargeUtility, chargeUtilityBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return chargeUtility;
    }

    @Override
    public void operationCanceled() {
        chargeUtility = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
