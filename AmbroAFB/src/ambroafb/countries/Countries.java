/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class Countries extends ListingStage {
    
    private CountriesController countriesController;
    
    public Countries(Stage owner){
        super(owner, StringUtils.substringAfterLast(Countries.class.toString(), "."), "countries", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/countries/Countries.fxml", null);
        countriesController = (CountriesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            countriesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            event.consume();
        });
        
        super.setFeatures(() -> countriesController.getEditorPanelController().getPanelMinWidth());
        
    }
    
    public CountriesController getCountriesController(){
        return countriesController;
    }
}
