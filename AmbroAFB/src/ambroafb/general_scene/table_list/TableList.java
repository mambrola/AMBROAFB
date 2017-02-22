/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class TableList extends ListingStage {
    
    private TableListController tableListController;
    
    public TableList(Stage owner, Class tableContent, String stageTitleBundleKey){
        super(owner, tableContent.getSimpleName(), stageTitleBundleKey);
        
        Scene scene = SceneUtils.createScene("/ambroafb/general_scene/table_list/TableList.fxml", null);
        tableListController = (TableListController) scene.getProperties().get("controller");
        tableListController.addTableByClass(tableContent);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            tableListController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> tableListController.getEditorPanelController().getPanelMinWidth());
    }
    
    public TableListController getController(){
        return tableListController;
    }
}
