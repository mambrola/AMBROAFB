/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelableSceneStage;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class CurrencyRates extends EditorPanelableSceneStage {
    
    private final CurrencyRatesController currencyRatesController;
    
    public CurrencyRates(Stage owner){
        super(owner);
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + getClass().getSimpleName(), (Stage)this);
//        Utils.centerStageOfParent((Stage)this, owner);
        
        Scene scene = Utils.createScene("/ambroafb/currency_rates/CurrencyRates.fxml", null);
        currencyRatesController = (CurrencyRatesController) scene.getProperties().get("controller");
        this.setScene(scene);
        super.setController(currencyRatesController);
//        this.initOwner(owner);

        Utils.setSizeFor((Stage)this);
    }
    
    public CurrencyRatesController getCurrencyRatesController(){
        return currencyRatesController;
    }
}
