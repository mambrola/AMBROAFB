/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.minitables.attitudes.Attitude;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class AttitudeDialog extends UserInteractiveDialogStage implements Dialogable {

    private Attitude attitude;
    private final Attitude attitudeBackup;
    
    private DialogController dialogController;
    
    public AttitudeDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "attitude");
        
        if (object == null)
            this.attitude = new Attitude();
        else
            this.attitude = (Attitude) object;
        this.attitudeBackup = attitude.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/attitudes/dialog/AttitudeDialog.fxml", null);
        dialogController = (AttitudeDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(attitude, attitudeBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return attitude;
    }

    @Override
    public void operationCanceled() {
        attitude = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}