/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
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
public class CurrencyRates extends Stage {
    
    private final CurrencyRatesController currencyRatesController;
    
    public CurrencyRates(Stage owner){
        StagesContainer.registerStageByOwner(owner, getClass().getSimpleName(), (Stage)this);
        
        this.setTitle("currencyRates");
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/CurrencyRates.fxml", null);
        currencyRatesController = (CurrencyRatesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            Stage currencyRateFilter = StagesContainer.getStageFor(this, Names.LEVEL_FOR_PATH);
            if (currencyRateFilter != null && currencyRateFilter.isShowing()){
                currencyRateFilter.getOnCloseRequest().handle(null);
            }
            else {
                currencyRatesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            }
            if(event != null) event.consume();
        });

        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> currencyRatesController.getEditorPanelController().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public CurrencyRatesController getCurrencyRatesController(){
        return currencyRatesController;
    }
}
