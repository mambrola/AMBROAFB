/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Dato
 */
public class Licenses extends ListingStage {
    
    private LicensesController licensesController;
    
    public  Licenses(Stage owner){
        super(owner, Licenses.class.getSimpleName(), "licenses");
        
        Scene scene = SceneUtils.createScene("/ambroafb/licenses/Licenses.fxml", null);
        licensesController = (LicensesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            licensesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> licensesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public LicensesController getLicensesController(){
        return licensesController;
    }
}
