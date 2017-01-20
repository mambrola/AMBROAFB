/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class DiscountOnCounts extends ListingStage {
    
    private DiscountOnCountsController discountsController;
    
    public DiscountOnCounts(Stage owner){
        super(owner, StringUtils.substringAfterLast(DiscountOnCounts.class.toString(), "."), "discounts_on_count", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/discounts_on_count/DiscountOnCounts.fxml", null);
        discountsController = (DiscountOnCountsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            discountsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> discountsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public DiscountOnCountsController getDiscountOnCountsController(){
        return discountsController;
    }
}
