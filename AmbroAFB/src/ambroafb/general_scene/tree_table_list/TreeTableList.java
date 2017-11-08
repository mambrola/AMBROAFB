/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.tree_table_list;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.ListingStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class TreeTableList extends ListingStage {
    
    public TreeTableList(Stage owner, Class treeContent, String stageTitleBundleKey){
        this(owner, treeContent,  stageTitleBundleKey, new StandardEditorPanel());
    }
    
    public TreeTableList(Stage owner, Class treeContent, String stageTitleBundleKey, EditorPanel editorPanel){
        super(owner, "/ambroafb/general_scene/tree_table_list/TreeTableList.fxml", treeContent, stageTitleBundleKey, editorPanel);
    }
}
