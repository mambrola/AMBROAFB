/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.dialog;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.minitables.MiniTable;
import ambroafb.minitables.attitudes.Attitude;
import ambroafb.minitables.merchandises.Merchandise;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class MiniTableDialog extends UserInteractiveDialogStage implements Dialogable {

    private MiniTable miniTable;
    private final MiniTable miniTableBackup;
    
    public MiniTableDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "/ambroafb/minitables/dialog/MiniTableDialog.fxml", "");
        
        if (object == null)
            miniTable = getConcreteMinitableFrom(object);
        else
            miniTable = (MiniTable) object;
        miniTableBackup = (MiniTable)miniTable.cloneWithID();
        
        dialogController.setSceneData(miniTable, miniTableBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return miniTable;
    }

    @Override
    public void operationCanceled() {
        miniTable = null;
    }
    
    public MiniTableDialogController getDialogController(){
        return (MiniTableDialogController)dialogController;
    }
    
    private MiniTable getConcreteMinitableFrom(EditorPanelable object) {
        MiniTable result = new Attitude();
        if (object instanceof Merchandise) {
            result = new Merchandise();
        }
        return result;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
}
