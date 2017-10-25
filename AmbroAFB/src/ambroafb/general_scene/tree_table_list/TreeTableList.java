/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.tree_table_list;

import ambroafb.general.interfaces.ListingStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class TreeTableList extends ListingStage {
    
//    private TreeTableListController treeTableListController;
    
    public TreeTableList(Stage owner, Class treeContent, String stageTitleBundleKey){
        super(owner, "/ambroafb/general_scene/tree_table_list/TreeTableList.fxml", treeContent,  stageTitleBundleKey);
        
//        Scene scene = SceneUtils.createScene("/ambroafb/general_scene/tree_table_list/TreeTableList.fxml", null);
//        treeTableListController = (TreeTableListController) scene.getProperties().get("controller");
//        treeTableListController.addTreeTableByClass(treeContent);
//        this.setScene(scene);
//        
//        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
//            treeTableListController.getEditorPanelController().getExitButton().getOnAction().handle(null);
//            if(event != null) event.consume();
//        });
//        
//        super.setFeatures(() -> treeTableListController.getEditorPanelController().getPanelMinWidth());
    }
    
}
