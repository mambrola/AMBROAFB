/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables;

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
public class MiniTables extends ListingStage {
    
    private MiniTablesController miniTablesController;
    
    public MiniTables(Stage owner, String contentClassFullName, String stageLocalizableTitle){
        super(owner, contentClassFullName, stageLocalizableTitle, "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/minitables/MiniTables.fxml", null);
        miniTablesController = (MiniTablesController) scene.getProperties().get("controller");
//        miniTablesController.setTableInitClass(contentClassFullName);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            miniTablesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> miniTablesController.getEditorPanelController().getPanelMinWidth());
    }
}
