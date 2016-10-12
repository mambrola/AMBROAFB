/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambroafb.currencies.Currency;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Names.*;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.save_button.StageUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class CurrencyDialog extends Stage implements Dialogable {

    private Currency currency;
    private final Currency currencyBackup;
    
    private CurrencyDialogController dialogController;
    
    public CurrencyDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner){
        Utils.registerStageByOwner(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        
        if (object == null)
            this.currency = new Currency();
        else
            this.currency = (Currency) object;
        this.currencyBackup = currency.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/currencies/dialog/CurrencyDialog.fxml", null);
        dialogController = (CurrencyDialogController) currentScene.getProperties().get("controller");
        dialogController.bindCurrency(this.currency);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupCurrency(this.currencyBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("currency_dialog_title"));
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
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
    
}
