/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names.*;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class DiscountOnCountDialog extends Stage implements Dialogable {
    
    public DiscountOnCount discountOnCount;
    public final DiscountOnCount discountOnCountBackup;
    
    private DiscountOnCountDialogController dialogController;
    
    public DiscountOnCountDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        String discountOnCountDialogPath = Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH;
        Utils.saveShowingStageByPath(discountOnCountDialogPath, (Stage)this);
        
        if (object == null)
            discountOnCount = new DiscountOnCount();
        else
            discountOnCount = (DiscountOnCount) object; 
        
        this.discountOnCountBackup = discountOnCount.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/discounts_on_count/dialog/DiscountOnCountDialog.fxml", null);
        dialogController = (DiscountOnCountDialogController) currentScene.getProperties().get("controller");
        dialogController.bindDiscountOnCount(this.discountOnCount); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupDiscountOnCount(this.discountOnCountBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("discount_on_count_dialog_title"));

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
