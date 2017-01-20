/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

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
public class CurrencyRates extends ListingStage {
    
    private final CurrencyRatesController currencyRatesController;
    
    public CurrencyRates(Stage owner){
        super(owner, StringUtils.substringAfterLast(CurrencyRates.class.toString(), "."), "currency_rates", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/CurrencyRates.fxml", null);
        currencyRatesController = (CurrencyRatesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            currencyRatesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });

        super.setFeatures(() -> currencyRatesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public CurrencyRatesController getCurrencyRatesController(){
        return currencyRatesController;
    }
}
