/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class CustomDialog extends UserInteractiveDialogStage implements Dialogable {

    private Doc doc;
    private final Doc docBackup;
    
    private List<Doc> docs = new ArrayList<>();
    
    public CustomDialog(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE buttonType, EditorPanelable object) {
        super(owner, buttonType, "/ambroafb/docs/types/custom/dialog/CustomDialog.fxml");
        
        if (object == null)
            doc = new Doc();
        else
            doc = (Doc) object;
        docBackup = doc.cloneWithID();
        
        dialogController.setSceneData(doc, docBackup, buttonType);
    }

    @Override
    public List<Doc> getResult() {
        showAndWait();
        return docs;
    }

    @Override
    public void operationCanceled() {
        docs.clear();
    }

    @Override
    public Doc getSceneObject(){
        return doc;
    }

    @Override
    protected Consumer<Void> getDeleteSuccessAction() {
        return (Void) -> {
                    docs.clear();
                    docs.add(doc);
                };
    }
    
    

    @Override
    protected Consumer<Object> getEditSuccessAction() {
        return getAddSuccessAction();
    }

    
    
    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return (obj) -> {
                    docs.clear();
                    docs.add((Doc)obj);
                };
    }
    
    
}
