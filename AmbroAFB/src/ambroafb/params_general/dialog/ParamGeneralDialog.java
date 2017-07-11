/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.params_general.ParamGeneral;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class ParamGeneralDialog extends UserInteractiveStage implements Dialogable {
    
    private ParamGeneral paramGeneral;
    private final ParamGeneral paramGeneralBackup;
    
    private ParamGeneralDialogController dialogController;
    
    public ParamGeneralDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "param_general_dialog_title", "/images/dialog.png");
        
        ParamGeneral param;
        if (object == null)
            param = new ParamGeneral();
        else
            param = (ParamGeneral) object;
        
        this.paramGeneral = param;
        this.paramGeneralBackup = param.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/params_general/dialog/ParamGeneralDialog.fxml", null);
        dialogController = (ParamGeneralDialogController) currentScene.getProperties().get("controller");
        dialogController.bindParamGeneral(this.paramGeneral);
        dialogController.setBackupParamGeneral(this.paramGeneralBackup);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }

    @Override
    public ParamGeneral getResult() {
        showAndWait();
        return paramGeneral;
    }

    @Override
    public void operationCanceled() {
        paramGeneral = null;
    }
    
}
