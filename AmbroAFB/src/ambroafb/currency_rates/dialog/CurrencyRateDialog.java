/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambroafb.currency_rates.CurrencyRate;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class CurrencyRateDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private CurrencyRate currRate;
    private final CurrencyRate currRateBackup;
    
    public CurrencyRateDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/currency_rates/dialog/CurrencyRateDialog.fxml", "currency_rate_dialog_title");
        
        if (object == null)
            currRate = new CurrencyRate();
        else 
            currRate = (CurrencyRate) object;
        currRateBackup = currRate.cloneWithID();
        
        dialogController.setSceneData(currRate, currRateBackup, buttonType);
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return currRate;
    }

    @Override
    public void operationCanceled() {
        currRate = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
    
}
