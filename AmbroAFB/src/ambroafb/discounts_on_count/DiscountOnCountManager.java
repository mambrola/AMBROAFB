/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambroafb.discounts_on_count.dialog.DiscountOnCountDialog;
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
public class DiscountOnCountManager extends EditorPanelableManager {

    public DiscountOnCountManager(){
        dataFetchProvider = new DiscountOnCountDataFetchProvider();
        dataChangeProvider = new DiscountOnCountDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        DiscountOnCountDialog dialog = new DiscountOnCountDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "discounts_on_count");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
