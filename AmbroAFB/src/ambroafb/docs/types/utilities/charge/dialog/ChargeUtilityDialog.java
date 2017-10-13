/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge.dialog;

import ambroafb.docs.types.utilities.charge.ChargeUtility;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class ChargeUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private ChargeUtility chargeUtility;
    private final ChargeUtility chargeUtilityBackup;
    
    public ChargeUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/docs/types/utilities/charge/dialog/ChargeUtilityDialog.fxml", "doc_charge_utility_dialog_title");
        
        if (object == null)
            chargeUtility = new ChargeUtility();
        else
            chargeUtility = (ChargeUtility) object;
        chargeUtilityBackup = chargeUtility.cloneWithID();
        
        dialogController.setSceneData(chargeUtility, chargeUtilityBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return chargeUtility;
    }

    @Override
    public void operationCanceled() {
        chargeUtility = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
