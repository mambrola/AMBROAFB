/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.configuration;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class Configuration extends UserInteractiveStage {
    
    private final ConfigurationController configController;
    
    public Configuration(Stage owner){
        super(owner, Names.CONFIGURATION, "menu_main_config", "/images/configuration.png");
        
        Scene scene = SceneUtils.createScene(Names.CONFIGURATION_FXML, null);
        configController = (ConfigurationController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            configController.getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public ConfigurationController getConfigurationController(){
        return configController;
    }
}
