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
        return conversionDocs;
    }

    @Override
    public void operationCanceled() {
        conversionDocs.clear();
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return conversion;
    }

    @Override
    protected Consumer<Void> getDeleteSuccessAction() {
        return (Void) -> {
                        conversionDocs.clear();
                        conversionDocs.addAll(deserializeConversion(conversion));
                    };
    }
    
    private List<Doc> deserializeConversion(Conversion conversion){
        List<Doc> result = new ArrayList<>();
        Doc firstDoc = new Doc();
        firstDoc.setRecId(conversion.getRecId());
        firstDoc.setDocDate(conversion.getDocDate());
        firstDoc.setDocInDocDate(conversion.getDocInDocDate());
        firstDoc.setDescrip(conversion.descripProperty().get());
        
        Doc secondDoc = new Doc();
        secondDoc.copyFrom(firstDoc);
        
        firstDoc.setIso(conversion.getSellCurrency());
        firstDoc.setAmount(conversion.getSellAmount());
        firstDoc.debitProperty().set(conversion.getSellAccount());
        
        secondDoc.setParentRecId(conversion.getRecId());
        secondDoc.setIso(conversion.getBuyingCurrency());
        secondDoc.debitProperty().set(conversion.getBuyingAccount());
        secondDoc.setAmount(conversion.getBuyingAmount());
        
        result.add(firstDoc);
        result.add(secondDoc);
        return result;
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
