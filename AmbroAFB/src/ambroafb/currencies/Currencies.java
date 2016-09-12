/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.Utils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class Currencies extends Stage {
    
    private CurrenciesController currenciesController;
    
    public Currencies(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + getClass().getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/currencies/Currencies.fxml", null);
        currenciesController = (CurrenciesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            currenciesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        Utils.setSizeFor((Stage)this);
    }
    
    public CurrenciesController getCurrenciesController(){
        return currenciesController;
    }
}
