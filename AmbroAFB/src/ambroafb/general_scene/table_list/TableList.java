/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.ListingStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class TableList extends ListingStage {
    
    public TableList(Stage owner, Class tableContent, String stageTitleBundleKey){
        this(owner, tableContent, stageTitleBundleKey, new StandardEditorPanel());
    }
    
    public TableList(Stage owner, Class tableContent, String stageTitleBundleKey, EditorPanel editorPanel){
        super(owner, "/ambroafb/general_scene/table_list/TableList.fxml", tableContent, stageTitleBundleKey, editorPanel);
    }
    
}
