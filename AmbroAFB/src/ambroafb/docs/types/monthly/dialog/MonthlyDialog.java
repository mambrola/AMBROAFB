/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly.dialog;

import ambroafb.docs.types.monthly.Monthly;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class MonthlyDialog extends UserInteractiveStage implements Dialogable{

    private Monthly monthly, monthlyBackup;
    
    private MonthlyDialogController dialogController;
    
    public MonthlyDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, Names.LEVEL_FOR_PATH, "charge_utility_dialog_title", "/images/dialog.png");
        
        if (object == null){
            monthly = new Monthly();
        }
        else {
            monthly = (Monthly) object;
        }
        monthlyBackup = (Monthly) monthly.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/monthly/dialog/MonthlyDialog.fxml", null);
        dialogController = (MonthlyDialogController) currentScene.getProperties().get("controller");
        dialogController.bindMonthly(this.monthly); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupCharge(this.monthlyBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return monthly;
    }

    @Override
    public void operationCanceled() {
        monthly = null;
    }
    
}
