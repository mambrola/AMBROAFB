/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.stages.UserInteractiveStage;
import ambroafb.invoices.Invoice;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class InvoiceDialog extends UserInteractiveStage implements Dialogable {

    private Invoice invoice;
    private final Invoice invoiceBackup;
    
    private InvoiceDialogController dialogController;
    
    public InvoiceDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "invoice_dialog_title", "/images/dialog.png");
        
        Invoice invoiceObject;
        if (object == null)
            invoiceObject = new Invoice();
        else
            invoiceObject = (Invoice) object;
        
        this.invoice = invoiceObject;
        this.invoiceBackup = invoiceObject.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/invoices/dialog/InvoiceDialog.fxml", null);
        dialogController = (InvoiceDialogController) currentScene.getProperties().get("controller");
        dialogController.bindInvoice(this.invoice);
        dialogController.setBackupInvoice(this.invoiceBackup);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        this.setScene(currentScene);
        
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
