/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceReissuing;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author dato
 */
public class InvoiceDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker createdDate, beginDate;
    @FXML
    private CheckBox revoked;
    @FXML
    private TextField invoiceNumber, additionalDiscount, paidPart, vat;
    @FXML
    private ClientComboBox clients;
//    @FXML
//    private ComboBox<License licenses;
    @FXML
    private ComboBox<Integer> monthCount;
    @FXML
    private ComboBox<InvoiceReissuing> invoiceReissuings;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Invoice invoice;
    private Invoice invoiceBackup;
    private boolean permissionToClose;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        permissionToClose = true;
    }

    public void bindInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null){
            createdDate.setValue(invoice.getLocalDateObj());
            System.out.println("invoice.inviceNumber: " + invoice.invoiceNumberProperty().get());
            invoiceNumber.textProperty().bindBidirectional(invoice.invoiceNumberProperty());
            clients.valueProperty().bindBidirectional(invoice.clientProperty());
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
//            revoked.
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            paidPart.textProperty().bindBidirectional(invoice.paidPartProperty());
            vat.textProperty().bindBidirectional(invoice.vatProperty());
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        boolean editable = true;
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            editable = false;
        }
        if (!buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            specificDisabledComponents();
        }
//        Client client = invoice.clientProperty().get();
//        if (client != null){
//            Client appropClient = clients.getItems().filtered((Client currClient) -> currClient.getRecId() == client.getRecId()).get(0);
//            clients.setValue(appropClient);
//        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    /**
     * Disables all fields on Dialog stage.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void specificDisabledComponents(){
        invoiceNumber.setDisable(true);
        paidPart.setDisable(true);
        vat.setDisable(true);
    }

    public void setBackupInvoice(Invoice invoiceBackup) {
        this.invoiceBackup = invoiceBackup;
    }

    public boolean anyComponentChanged(){
        return !invoice.compares(invoiceBackup);
    }

    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    
    
}
