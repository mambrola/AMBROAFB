/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.general.Utils;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class CurrencyRates extends Stage {
    
    private CurrencyRatesController CurrencyRatesController;
    
    public CurrencyRates(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + getClass().getSimpleName(), (Stage)this);
        Utils.centerStageOfParent((Stage)this, owner);
        
        Scene scene = Utils.createScene("/ambroafb/currency_rates/CurrencyRates.fxml", null);
        CurrencyRatesController = (CurrencyRatesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            CurrencyRatesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });

        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            double edPanMinWidth = CurrencyRatesController.getEditorPanelController().getPanelMinWidth();
            double stageMinWidth = edPanMinWidth + 20;
            if (newValue.doubleValue() < stageMinWidth){
                this.setMinWidth(stageMinWidth);
            }
        });
        
        Utils.setSizeFor((Stage)this);
    }
    
    public CurrencyRatesController getCurrencyRatesController(){
        return CurrencyRatesController;
    }
}
