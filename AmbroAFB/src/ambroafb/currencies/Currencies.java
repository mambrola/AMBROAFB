/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

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
public class Currencies extends ListingStage {
    
    private CurrenciesController currenciesController;
    
    public Currencies(Stage owner){
        super(owner, Currencies.class.getSimpleName(), "currencies");
        
        Scene scene = SceneUtils.createScene("/ambroafb/currencies/Currencies.fxml", null);
        currenciesController = (CurrenciesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            currenciesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        super.setFeatures(() -> currenciesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public CurrenciesController getCurrenciesController(){
        return currenciesController;
    }
}
