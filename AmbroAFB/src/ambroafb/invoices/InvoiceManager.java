/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.invoices.dialog.InvoiceDialog;
import ambroafb.invoices.filter.InvoiceFilter;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class InvoiceManager extends EditorPanelableManager {

    public InvoiceManager(){
        dataFetchProvider = new InvoiceDataFetchProvider();
        dataChangeProvider = new InvoiceDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        InvoiceDialog dialog = new InvoiceDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "invoice_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new InvoiceFilter(owner);
    }
    
}
