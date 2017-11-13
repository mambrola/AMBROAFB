/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.conversion.Conversion;
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
public class ConversionDialog extends UserInteractiveDialogStage implements Dialogable {

    private Conversion conversion;
    private final Conversion conversionBackup;
    
    private final List<Doc> docs = new ArrayList<>();
    
    public ConversionDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, buttonType, "/ambroafb/docs/types/conversion/dialog/ConversionDialog.fxml");
        
        if (object == null)
            conversion = new Conversion();
        else 
            conversion = (Conversion)object;
        conversionBackup = conversion.cloneWithID();
        
        dialogController.setSceneData(conversion, conversionBackup, buttonType);
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
    protected EditorPanelable getSceneObject() {
        return conversion;
    }

    @Override
    protected Consumer<Object> getDeleteSuccessAction() {
        return (obj) -> {
                        docs.clear();
                        docs.addAll((List<Doc>)obj);
//                        docs.addAll(conversion.convertToDoc());
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
                    docs.addAll((List<Doc>)obj);
                };
    }


    
}
