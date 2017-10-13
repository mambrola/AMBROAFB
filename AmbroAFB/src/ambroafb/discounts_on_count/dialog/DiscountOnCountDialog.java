/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
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
public class DiscountOnCountDialog extends UserInteractiveDialogStage implements Dialogable {
    
    public DiscountOnCount discountOnCount;
    public final DiscountOnCount discountOnCountBackup;
    
    private DialogController dialogController;
    
    public DiscountOnCountDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "discounts_on_count");
        
        if (object == null)
            discountOnCount = new DiscountOnCount();
        else
            discountOnCount = (DiscountOnCount) object; 
        
        this.discountOnCountBackup = discountOnCount.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/discounts_on_count/dialog/DiscountOnCountDialog.fxml", null);
        dialogController = (DiscountOnCountDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(discountOnCount, discountOnCountBackup, buttonType);
        this.setScene(currentScene);

        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }
    
    @Override
    public DiscountOnCount getResult() {
        showAndWait();
        return discountOnCount;
    }
    
    @Override
    public void operationCanceled(){
        discountOnCount = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
}
