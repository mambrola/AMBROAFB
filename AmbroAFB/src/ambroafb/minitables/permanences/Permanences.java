/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.permanences;

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
public class Permanences extends ListingStage {
    
    private PermanencesController permanencesController;
    
    public Permanences(Stage owner) {
        super(owner, Permanences.class.getSimpleName(), "permanence", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/minitables/permanences/Permanences.fxml", null);
        permanencesController = (PermanencesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            permanencesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> permanencesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public PermanencesController getPermanencesController(){
        return permanencesController;
    }
}
