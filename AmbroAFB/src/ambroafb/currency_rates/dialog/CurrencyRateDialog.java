/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambroafb.currency_rates.CurrencyRate;
import ambroafb.general.editor_panel.EditorPanel;
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
    
    public CurrencyRateDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, buttonType, "/ambroafb/currency_rates/dialog/CurrencyRateDialog.fxml");
        
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
    protected CurrencyRate getSceneObject() {
        return currRate;
    }

}
