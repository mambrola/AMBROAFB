/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.params_general.dialog.ParamGeneralDialog;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class ParamGeneralManager extends EditorPanelableManager {

    public ParamGeneralManager(){
        dataFetchProvider = new ParamGeneralDataFetchProvider();
        dataChangeProvider = new ParamGeneralDataChangeProvider(dataFetchProvider);
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        ParamGeneralDialog dialog = new ParamGeneralDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "param_general_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
