/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

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
public class Loggings extends Stage {
    
    private LoggingsController loggingsController;
    
    public Loggings(Stage owner){
        StagesContainer.registerStageByOwner(owner, getClass().getSimpleName(), (Stage)this);
        
        Scene scene = SceneUtils.createScene("/ambroafb/loggings/Loggings.fxml", null);
        loggingsController = (LoggingsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);  
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            Stage loggingsFilter = StagesContainer.getStageFor(this, Names.LEVEL_FOR_PATH);
            if (loggingsFilter != null && loggingsFilter.isShowing()){
                loggingsFilter.getOnCloseRequest().handle(null);
            }
            else {
                loggingsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            }
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> loggingsController.getEditorPanelController().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public LoggingsController getLoggingsController(){
        return loggingsController;
    }
}
