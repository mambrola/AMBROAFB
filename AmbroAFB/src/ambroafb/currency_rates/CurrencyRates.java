/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.general.Utils;
import ambroafb.general.save_button.StageUtils;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class CurrencyRates extends Stage {
    
    private final CurrencyRatesController currencyRatesController;
    
    public CurrencyRates(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + getClass().getSimpleName(), (Stage)this);
        Utils.setSizeFor((Stage)this);
        
        this.setTitle("currencyRates");
        Scene scene = Utils.createScene("/ambroafb/currency_rates/CurrencyRates.fxml", null);
        currencyRatesController = (CurrencyRatesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);

        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildToOwner(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> currencyRatesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public CurrencyRatesController getCurrencyRatesController(){
        return currencyRatesController;
    }
}
