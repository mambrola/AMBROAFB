/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

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
 * @author Dato
 */
public class Licenses extends Stage {
    
    private LicensesController licensesController;
    
    public  Licenses(Stage owner){
        StagesContainer.registerStageByOwner(owner, getClass().getSimpleName(), (Stage)this);
        
        Scene scene = SceneUtils.createScene("/ambroafb/licenses/Licenses.fxml", null);
        licensesController = (LicensesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            Stage licenseFilter = StagesContainer.getStageFor(this, Names.LEVEL_FOR_PATH);
            if (licenseFilter != null && licenseFilter.isShowing()){
                licenseFilter.getOnCloseRequest().handle(null);
            }
            else {
                licensesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            }
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> licensesController.getEditorPanelController().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public LicensesController getLicensesController(){
        return licensesController;
    }
}
