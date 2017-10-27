/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.doc_table_list;

import ambroafb.general.interfaces.ListingStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class DocTableList extends ListingStage {
    
//    private DocTableListController tableListController;
    
    public DocTableList(Stage owner, Class tableContent, String stageTitleBundleKey){
        super(owner, "/ambroafb/general_scene/doc_table_list/DocTableList.fxml", tableContent, stageTitleBundleKey);
        
//        Scene scene = SceneUtils.createScene("/ambroafb/general_scene/doc_table_list/DocTableList.fxml", null);
//        tableListController = (DocTableListController) scene.getProperties().get("controller");
//        tableListController.addTableByClass(tableContent);
        
//        this.setScene(scene);
//        
//        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
//            tableListController.getDocEditorPanelController().getExitButton().getOnAction().handle(null);
//            if(event != null) event.consume();
//        });
//        
//        super.setFeatures(() -> tableListController.getDocEditorPanelController().getPanelMinWidth());
    }
    
//    public DocTableListController getDocController(){
//        return tableListController;
//    }
}
