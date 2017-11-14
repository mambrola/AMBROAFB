/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambroafb.currencies.Currency;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.function.Consumer;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class CurrencyDialog extends UserInteractiveDialogStage implements Dialogable {

    private Currency currency;
    private final Currency currencyBackup;
    
    public CurrencyDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, buttonType, "/ambroafb/currencies/dialog/CurrencyDialog.fxml");
        
        if (object == null)
            currency = new Currency();
        else
            currency = (Currency) object;
        currencyBackup = currency.cloneWithID();
        
        dialogController.setSceneData(currency, currencyBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return currency;
    }

    @Override
    public void operationCanceled() {
        currency = null;
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return currency;
    }

    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return (obj) -> currency = (Currency) obj;
    }
    
    
}
