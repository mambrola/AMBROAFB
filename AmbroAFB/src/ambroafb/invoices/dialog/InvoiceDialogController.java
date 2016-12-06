/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.monthcountercombobox.MonthCounterComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBox;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.products.Product;
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
import ambroafb.general.interfaces.Annotations.*;

/**
 *
 * @author dato
 */
public class InvoiceDialogController implements Initializable {

    // The order is needed for required components show ordering on scene:
    // start order:
    @FXML @ContentNotEmpty
    private ClientComboBox clients;
    @FXML @ContentNotEmpty
    private CountComboBox<Product> licenses;
    @FXML @ContentNotEmpty
    private ADatePicker beginDate;
    @FXML @ContentNotEmpty
    private MonthCounterComboBox monthCounter;
    @FXML @ContentPattern(value = "(([1-9]\\d*)?\\d)(\\.\\d\\d)?$", explain = "The discount syntax is incorrect. like: 0.35, 3.25 ...")
    private TextField additionalDiscount;
    @FXML @ContentNotEmpty
    private ComboBox<InvoiceReissuing> invoiceReissuings;
    // end order
    
    
    @FXML
    private VBox formPane;
    @FXML
    private ADatePicker createdDate, endDate;
    @FXML
    private CheckBox revoked;
    @FXML
    private TextField invoiceNumber, moneyToPay, moneyPaid, vat, status;
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
        clients.registerBundle(resources);
        clients.showCategoryALL(false);
        licenses.getItems().addAll(Product.getAllFromDB());
        permissionToClose = true;
    }

    public void bindInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null){
            createdDate.setValue(invoice.getCreatedDateObj());
            invoiceNumber.textProperty().bindBidirectional(invoice.invoiceNumberProperty());
            clients.valueProperty().bindBidirectional(invoice.clientProperty());
            //
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
            monthCounter.valueProperty().bindBidirectional(invoice.monthsProperty());
            endDate.valueProperty().bindBidirectional(invoice.endDateProperty());
            revoked.selectedProperty().bindBidirectional(invoice.revokedProperty());
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            moneyToPay.textProperty().bindBidirectional(invoice.moneyToPayProperty());
            moneyPaid.textProperty().bindBidirectional(invoice.moneyPaidProperty());
            vat.textProperty().bindBidirectional(invoice.vatProperty());
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            status.textProperty().bindBidirectional(invoice.statusProperty().get().descripProperty());
            licenses.resultProperty().bindBidirectional(invoice.licensesResultProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        boolean editable = true;
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            editable = false;
        }
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            revoked.setDisable(true);
        }
        okayCancelController.setButtonsFeatures(buttonType);
//        if (invoice != null){
//            licenses.getItems().addAll(invoice.getLicenses());
//        }
    }
    
    /**
     * Disables all fields on Dialog stage.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
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
