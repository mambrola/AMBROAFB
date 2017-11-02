/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class CustomDialog extends UserInteractiveDialogStage implements Dialogable {

    private Doc doc;
    private final Doc docBackup;
    
    public CustomDialog(Stage owner, Names.EDITOR_BUTTON_TYPE buttonType, EditorPanelable object) {
        super(owner, buttonType, "/ambroafb/docs/types/custom/dialog/CustomDialog.fxml");
        
        if (object == null)
            doc = new Doc();
        else
            doc = (Doc) object;
        docBackup = doc.cloneWithID();
        
        dialogController.setSceneData(doc, docBackup, buttonType);
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
    public Doc getSceneObject(){
        return doc;
    }
}
