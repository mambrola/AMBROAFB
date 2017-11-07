/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom;

import ambroafb.docs.DocDataChangeProvider;
import ambroafb.docs.DocDataFetchProvider;
import ambroafb.docs.types.custom.dialog.CustomDialog;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class CustomManager extends EditorPanelableManager {
    
    public CustomManager(){
        dataFetchProvider = new DocDataFetchProvider();
        dataChangeProvider = new DocDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        CustomDialog dialog = new CustomDialog(owner, type, object);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "doc_custom_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
