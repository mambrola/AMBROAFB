/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.transfer;

import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.utilities.transfer.dialog.TransferUtilityDialog;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DocDialogable;
import ambroafb.general.interfaces.EditorPanelable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class TransferUtilityManager implements DocManager {
    
    private TransferUtility transferUtility, transferUtilityBackup;

    @Override
    public EditorPanelable getOneFromDB(int id) {
        return null;
    }

    @Override
    public EditorPanelable saveOneToDB(EditorPanelable newDocComponent) {
        return null;
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return false;
    }

    @Override
    public void undo() {
        
    }

    @Override
    public DocDialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type) {
        TransferUtilityDialog tranferDialog = new TransferUtilityDialog(transferUtility, type, owner);
        
        return tranferDialog;
    }
    
}
