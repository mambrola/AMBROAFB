/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambroafb.currency_rates.CurrencyRate;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.StageUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class CurrencyRateDialog extends Stage implements Dialogable {
    
    private CurrencyRate currRate;
    private final CurrencyRate currRateBackup;
    
    private CurrencyRateDialogController dialogController;
    
    public CurrencyRateDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        Utils.registerStageByOwner(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        
        if (object == null)
            currRate = new CurrencyRate();
        else 
            currRate = (CurrencyRate) object;
        currRateBackup = currRate.cloneWithID();
        
        Scene scene = Utils.createScene("/ambroafb/currency_rates/dialog/CurrencyRateDialog.fxml", null);
        dialogController = (CurrencyRateDialogController) scene.getProperties().get("controller");
        dialogController.bindCurrencyRate(this.currRate);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupCurrencyRate(this.currRateBackup);
        this.setScene(scene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("currency_rate_dialog_title"));
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
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
    
    
}
