/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.utilities.charge.ChargeUtility;
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
public class ChargeUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private ChargeUtility chargeUtility;
    private final ChargeUtility chargeUtilityBackup;
    
    private List<Doc> docs = new ArrayList<>();
    
    public ChargeUtilityDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, buttonType, "/ambroafb/docs/types/utilities/charge/dialog/ChargeUtilityDialog.fxml");
        
        if (object == null)
            chargeUtility = new ChargeUtility();
        else
            chargeUtility = (ChargeUtility) object;
        chargeUtilityBackup = chargeUtility.cloneWithID();
        
        dialogController.setSceneData(chargeUtility, chargeUtilityBackup, buttonType);
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
        return chargeUtility;
    }

    @Override
    protected Consumer<Void> getDeleteSuccessAction() {
        return (Void) -> {
                docs.clear();
                docs.addAll(chargeUtility.convertToDoc());
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
