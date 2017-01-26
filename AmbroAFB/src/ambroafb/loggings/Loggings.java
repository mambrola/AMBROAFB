/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

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
public class Loggings extends ListingStage {
    
    private LoggingsController loggingsController;
    
    public Loggings(Stage owner){
        super(owner, Loggings.class.getSimpleName(), "loggings");
        
        Scene scene = SceneUtils.createScene("/ambroafb/loggings/Loggings.fxml", null);
        loggingsController = (LoggingsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            loggingsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> loggingsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public LoggingsController getLoggingsController(){
        return loggingsController;
    }
}
