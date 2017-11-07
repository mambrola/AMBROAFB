/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.dialog;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.params_general.ParamGeneral;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class ParamGeneralDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private ParamGeneral paramGeneral;
    private final ParamGeneral paramGeneralBackup;
    
    public ParamGeneralDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/params_general/dialog/ParamGeneralDialog.fxml", "param_general_dialog_title");
        
        if (object == null)
            paramGeneral = new ParamGeneral();
        else
            paramGeneral = (ParamGeneral) object;
        this.paramGeneralBackup = paramGeneral.cloneWithID();
        
        dialogController.setSceneData(paramGeneral, paramGeneralBackup, buttonType);
    }

    @Override
    public ParamGeneral getResult() {
        showAndWait();
        return paramGeneral;
    }

    @Override
    public void operationCanceled() {
        paramGeneral = null;
    }

}
