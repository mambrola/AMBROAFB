/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambroafb.currencies.Currency;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class CurrencyDialog extends UserInteractiveDialogStage implements Dialogable {

    private Currency currency;
    private final Currency currencyBackup;
    
    private DialogController dialogController;
    
    public CurrencyDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "currency_dialog_title");
        
        if (object == null)
            this.currency = new Currency();
        else
            this.currency = (Currency) object;
        this.currencyBackup = currency.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/currencies/dialog/CurrencyDialog.fxml", null);
        dialogController = (CurrencyDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(currency, currencyBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
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
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
