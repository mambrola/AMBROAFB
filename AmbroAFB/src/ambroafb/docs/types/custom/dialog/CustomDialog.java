/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
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
public class CustomDialog extends UserInteractiveDialogStage implements Dialogable {

    private Doc doc, docBackup;
    
    private DialogController dialogController;
    
    public CustomDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "doc_custom_dialog_title");
        
        if (object == null){
            doc = new Doc();
        }
        else {
            doc = (Doc) object;
        }
        docBackup = doc.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/custom/dialog/CustomDialog.fxml", null);
        dialogController = (CustomDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(doc, docBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return doc;
    }

    @Override
    public void operationCanceled() {
        doc = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
