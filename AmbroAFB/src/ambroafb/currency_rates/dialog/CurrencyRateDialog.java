/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambroafb.currency_rates.CurrencyRate;
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
public class CurrencyRateDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private CurrencyRate currRate;
    private final CurrencyRate currRateBackup;
    
    private DialogController dialogController;
    
    public CurrencyRateDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "currency_rate_dialog_title");
        
        if (object == null)
            currRate = new CurrencyRate();
        else 
            currRate = (CurrencyRate) object;
        currRateBackup = currRate.cloneWithID();
        
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/dialog/CurrencyRateDialog.fxml", null);
        dialogController = (CurrencyRateDialogController) scene.getProperties().get("controller");
        dialogController.setSceneData(currRate, currRateBackup, buttonType);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
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
