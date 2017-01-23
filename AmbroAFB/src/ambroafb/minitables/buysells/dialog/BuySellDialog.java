/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.stages.UserInteractiveStage;
import ambroafb.minitables.buysells.BuySell;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BuySellDialog extends UserInteractiveStage implements Dialogable {
    
    private BuySell buySell;
    private final BuySell buySellBackup;
    
    private BuySellDialogController dialogController;
    
    public BuySellDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "", "/images/dialog.png");
        
        if (object == null)
            this.buySell = new BuySell();
        else
            this.buySell = (BuySell) object;
        this.buySellBackup = buySell.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/buysells/dialog/BuySellDialog.fxml", null);
        dialogController = (BuySellDialogController) currentScene.getProperties().get("controller");
        dialogController.bindCurrency(this.buySell);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupCurrency(this.buySellBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return buySell;
    }

    @Override
    public void operationCanceled() {
        buySell = null;
    }
}
