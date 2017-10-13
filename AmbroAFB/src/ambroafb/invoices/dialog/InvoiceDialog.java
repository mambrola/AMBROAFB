/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.invoices.Invoice;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class InvoiceDialog extends UserInteractiveDialogStage implements Dialogable {

    private Invoice invoice;
    private final Invoice invoiceBackup;
    
    public InvoiceDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/invoices/dialog/InvoiceDialog.fxml", "invoice_dialog_title");
        
        if (object == null)
            invoice = new Invoice();
        else
            invoice = (Invoice) object;
        invoiceBackup = invoice.cloneWithID();
        
        dialogController.setSceneData(invoice, invoiceBackup, buttonType);
    }

    @Override
    public Invoice getResult() {
        showAndWait();
        return invoice;
    }

    @Override
    public void operationCanceled() {
        invoice = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }

}
