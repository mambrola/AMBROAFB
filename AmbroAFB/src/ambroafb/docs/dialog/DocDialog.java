/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.dialog;

import ambroafb.docs.types.DocComponent;
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
public class DocDialog extends UserInteractiveStage implements DocDialogable {

    private DocDialogController dialogController;
    
    public DocDialog(DocComponent object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, Names.LEVEL_FOR_PATH, "doc_dialog_title", "/images/dialog.png");
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/dialog/DocDialog.fxml", null);
        dialogController = (DocDialogController) currentScene.getProperties().get("controller");
        dialogController.setNextVisibleAndActionParameters(object, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }

//    @Override
//    public DataDistributor getResult() {
//        showAndWait();
//        return dialogController.getDocComponent().getDocData();
//    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return null;
    }

    @Override
    public void operationCanceled() {

    }

}
