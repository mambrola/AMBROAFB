/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.minitables.merchandises.dialog.MerchandiseDialog;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class MerchandiseManager extends EditorPanelableManager {

    public MerchandiseManager() {
        dataFetchProvider = new MerchandiseDataFetchProvider();
        dataChangeProvider = new MerchandiseDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        MerchandiseDialog dialog = new MerchandiseDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "merchandise");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
