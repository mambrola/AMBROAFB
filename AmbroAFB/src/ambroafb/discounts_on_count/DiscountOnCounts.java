/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambroafb.general.Utils;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class DiscountOnCounts extends Stage {
    
    private DiscountOnCountsController discountsController;
    
    public DiscountOnCounts(Stage owner){
        StagesContainer.registerStageByOwner(owner, getClass().getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/discounts_on_count/DiscountOnCounts.fxml", null);
        discountsController = (DiscountOnCountsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            discountsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease(owner, () -> discountsController.getEditorPanelController().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public DiscountOnCountsController getDiscountOnCountsController(){
        return discountsController;
    }
}
