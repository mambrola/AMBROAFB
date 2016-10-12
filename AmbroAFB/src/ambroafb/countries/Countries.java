/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.Utils;
import ambroafb.general.save_button.StageUtils;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class Countries extends Stage {
    
    private CountriesController countriesController;
    
    public Countries(){}
    
    public Countries(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + Countries.class.getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/countries/Countries.fxml", null);
        countriesController = (CountriesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            countriesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        Utils.setSizeFor((Stage)this);
    }
    
    public CountriesController getCountriesController(){
        return countriesController;
    }
}
