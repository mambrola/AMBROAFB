/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BuySells extends ListingStage {
    
    private BuySellsController buySellsController;
    
    public BuySells(Stage owner) {
        super(owner, BuySells.class.getSimpleName(), "buysells", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/minitables/buysells/BuySells.fxml", null);
        buySellsController = (BuySellsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            buySellsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> buySellsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public BuySellsController getBuySellsController(){
        return buySellsController;
    }
}
