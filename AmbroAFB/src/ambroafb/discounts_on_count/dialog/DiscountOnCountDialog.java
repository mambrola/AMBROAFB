/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.Names.*;
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
 * @author dato
 */
public class DiscountOnCountDialog extends UserInteractiveStage implements Dialogable {
    
    public DiscountOnCount discountOnCount;
    public final DiscountOnCount discountOnCountBackup;
    
    private DiscountOnCountDialogController dialogController;
    
    public DiscountOnCountDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, Names.LEVEL_FOR_PATH, "discounts_on_count", "/images/dialog.png");
        
        if (object == null)
            discountOnCount = new DiscountOnCount();
        else
            discountOnCount = (DiscountOnCount) object; 
        
        this.discountOnCountBackup = discountOnCount.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/discounts_on_count/dialog/DiscountOnCountDialog.fxml", null);
        dialogController = (DiscountOnCountDialogController) currentScene.getProperties().get("controller");
        dialogController.bindDiscountOnCount(this.discountOnCount); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupDiscountOnCount(this.discountOnCountBackup);
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
}
