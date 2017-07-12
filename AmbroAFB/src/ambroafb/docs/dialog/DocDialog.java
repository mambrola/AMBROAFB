/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.dialog;

import ambroafb.docs.Doc;
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
 * @author dkobuladze
 */
public class DocDialog extends UserInteractiveStage implements Dialogable {

    public Doc doc;
    public final Doc docBackup;
    
    private DocDialogController dialogController;
    
    public DocDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner,  Names.LEVEL_FOR_PATH, "doc_dialog_title", "/images/dialog.png");
        
        if (object == null)
            doc = new Doc();
        else 
            doc = (Doc) object;
        
        docBackup = doc.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/dialog/DocDialog.fxml", null);
        dialogController = (DocDialogController) currentScene.getProperties().get("controller");
        dialogController.bindDoc(this.doc); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupDoc(this.docBackup);
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
    
}
