/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises.dialog;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.minitables.merchandises.Merchandise;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class MerchandiseDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private Merchandise merchandise;
    private final Merchandise merchandiseBackup;
    
    public MerchandiseDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/minitables/merchandises/dialog/MerchandiseDialog.fxml", "merchandise");
        
        if (object == null)
            merchandise = new Merchandise();
        else
            merchandise = (Merchandise) object;
        merchandiseBackup = merchandise.cloneWithID();
        
        dialogController.setSceneData(merchandise, merchandiseBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return merchandise;
    }

    @Override
    public void operationCanceled() {
        merchandise = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
