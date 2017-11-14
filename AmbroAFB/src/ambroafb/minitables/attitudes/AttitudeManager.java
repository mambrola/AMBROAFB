/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.minitables.attitudes.dialog.AttitudeDialog;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class AttitudeManager extends EditorPanelableManager {

    public AttitudeManager(){
        dataFetchProvider = new AttitudeDataFetchProvider();
        dataChangeProvider = new AttitudeDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        AttitudeDialog dialog = new AttitudeDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "attitude");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
