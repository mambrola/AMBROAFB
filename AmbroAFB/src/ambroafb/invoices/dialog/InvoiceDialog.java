/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.invoices.Invoice;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class InvoiceDialog extends Stage implements Dialogable {

    private Invoice invoice;
    private final Invoice invoiceBackup;
    
    private InvoiceDialogController dialogController;
    
    public InvoiceDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        Utils.centerStageOfParent((Stage)this, owner);
        
        Invoice invoiceObject;
        if (object == null)
            invoiceObject = new Invoice();
        else
            invoiceObject = (Invoice) object;
        
        this.invoice = invoiceObject;
        this.invoiceBackup = invoiceObject.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/invoices/dialog/InvoiceDialog.fxml", null);
        dialogController = (InvoiceDialogController) currentScene.getProperties().get("controller");
        dialogController.bindInvoice(this.invoice);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupInvoice(this.invoiceBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("invoice_dialog_title"));
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
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

}
