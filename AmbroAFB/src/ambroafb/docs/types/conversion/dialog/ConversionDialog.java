/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.conversion.Conversion;
import ambroafb.general.Names;
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
    
    private final List<Doc> conversionDocs = new ArrayList<>();
    
    public ConversionDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "/ambroafb/docs/types/conversion/dialog/ConversionDialog.fxml", "doc_conversion_dialog_title");
        
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
        return conversionDocs;
    }

    @Override
    public void operationCanceled() {
        
    }

    @Override
    protected Consumer<Object> getEditSuccessAction() {
        Consumer<Object> consumer = (obj) -> {
            conversionDocs.clear();
            conversionDocs.addAll((ArrayList<Doc>)obj);
        };
        return consumer;
    }

    
    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return getEditSuccessAction();
    }

    
}
