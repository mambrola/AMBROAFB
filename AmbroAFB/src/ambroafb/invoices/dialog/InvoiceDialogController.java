/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.DBUtils;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.monthcountercombobox.MonthCounterItem;
import ambroafb.invoices.helper.InvoiceFinaces;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.helper.LicenseFinaces;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private CountComboBox<Product> products;
    @FXML @ContentNotEmpty
    private ADatePicker beginDate;
    @FXML @ContentNotEmpty
    private MonthCounterComboBox monthCounter;
    @FXML @ContentPattern(value = "(([1-9]\\d*)?\\d)(\\.\\d\\d)?$", explain = "The discount syntax is incorrect. like: 0.35, 3.25 ...")
    private TextField additionalDiscount;
    // end order
    
    
    @FXML
    private VBox formPane, financesLabelTextContainer, financesLabelNumberContainer;
    @FXML
    private ComboBox<InvoiceReissuing> invoiceReissuings;
    @FXML
    private ADatePicker createdDate, endDate, revokedDate;
    @FXML
    private TextField invoiceNumber, status, licenses;
    @FXML
    private DialogOkayCancelController okayCancelController;
    @FXML
    private Label   sumText, sumNumber, discountText, discountNumber, 
                    netoText, netoNumber, vatText, vatNumber, payText, payNumber;
    
    
    private ArrayList<Node> focusTraversableNodes;
    private Invoice invoice;
    private Invoice invoiceBackup;
    private boolean permissionToClose;
    private String colonDelimiter = ":";
    private String percentDelimiter = "%";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        
        processInvoiceReissuingComboBox();
        
        clients.registerBundle(resources);
        clients.showCategoryALL(false);
        products.getItems().addAll(Product.getAllFromDB());
        permissionToClose = true;
        
        clients.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            if (oldValue != null){
                rebindFinanceData();
            }
        });
        products.showingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue != null && !newValue) {
                rebindFinanceData();
            }
        });
        beginDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (oldValue != null){
                rebindFinanceData();
            }
        });
        monthCounter.valueProperty().addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
            if (oldValue != null){
                rebindFinanceData();
            }
        });
        
        setFinanceInfosDefaultText();
    }
    
    private void processInvoiceReissuingComboBox(){
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        InvoiceReissuing defaultReissuing = invoiceReissuings.getItems().stream().filter((reissuing) -> reissuing.getRecId() == InvoiceReissuing.DEFAULT_REISSUING_ID).collect(Collectors.toList()).get(0);
        for (int i = 0; i < invoiceReissuings.getItems().size(); i++) {
            InvoiceReissuing reissuing = invoiceReissuings.getItems().get(i);
            if (reissuing.getRecId() == defaultReissuing.getRecId()){
                invoiceReissuings.setValue(reissuing);
                break;
            }
            
        }
                
            
        invoiceReissuings.setValue(defaultReissuing);
    }
    
    private void setFinanceInfosDefaultText(){
        sumText.setText(sumText.getText() + colonDelimiter);
        discountText.setText(discountText.getText() + percentDelimiter + colonDelimiter);
        netoText.setText(netoText.getText() + colonDelimiter);
        vatText.setText(vatText.getText() + percentDelimiter + colonDelimiter);
        payText.setText(payText.getText() + colonDelimiter);
    }
    
    private void setShowFinanceData(boolean isShow){
        financesLabelTextContainer.setVisible(isShow);
        financesLabelNumberContainer.setVisible(isShow);
    }
    
    private void rebindFinanceData(){
        if (isEveryNessesaryFieldValid()){
            System.out.println("into rebindFinanceData method...");
            if (!financesLabelTextContainer.isVisible()){
                setShowFinanceData(true);
            }
            Map<Product, Integer> productsMap = invoice.getProductsWithCounts();
            JSONArray productsArray = new JSONArray();
            productsMap.keySet().stream().forEach((product) -> {
                JSONObject json = Utils.getJsonFrom(null, "product_id", product.getRecId());
                productsArray.put(Utils.getJsonFrom(json, "count", productsMap.get(product)));
            });
            System.out.println("rebindFinanceData -> productsArray: " + productsArray);
            
            JSONArray licensesIds = new JSONArray();
            invoice.getLicenses().stream().forEach((licenseShortData) -> {
                licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.getLicense_id()));
            });
            System.out.println("licensesIds: " + licensesIds);
            
            String discount = invoice.getAdditionalDiscountRate();
            if (discount.isEmpty()){
                discount = null;
            }
            DBUtils.callInvoiceSuitedLicenses(null, invoice.getClientId(), invoice.beginDateProperty().get(), invoice.endDateProperty().get(), productsArray, discount, licensesIds);
            ArrayList<PartOfLicense> invoiceLicenses = DBUtils.getLicenses();
            ArrayList<LicenseFinaces> licenseFinaces = DBUtils.getLicensesFinaces();
            ArrayList<InvoiceFinaces> invoiceFinances = DBUtils.getInvoicesFinaces();
            
            invoice.setLicenseFinances(licenseFinaces);
            invoice.setInvoiceFinances(invoiceFinances);
            List<Invoice.LicenseShortData> wholeLicenses = invoiceLicenses.stream().map((license) -> {
                                                                        Invoice.LicenseShortData shortData = new Invoice.LicenseShortData();
                                                                        shortData.setLicenseId(license.invoiceLicenseId);
                                                                        shortData.setLicenseNumber(license.licenseNumber);
                                                                        return shortData;
                                                                }).collect(Collectors.toList());
//            System.out.println("invoice whole license: " + wholeLicenses);
            invoice.setLicenses(wholeLicenses);
            processFinanceData(invoiceFinances);
        }
        else {
            System.out.println("default value for labels ...");
        }
    }
    
    private void processFinanceData(ArrayList<InvoiceFinaces> invoiceFinances){
        if (invoiceFinances.isEmpty()) return;
        InvoiceFinaces financeOfInvoce = invoiceFinances.get(0);
        sumNumber.setText(financeOfInvoce.sum + financeOfInvoce.symbolTotal);
        discountText.setText(processString(discountText.getText(), financeOfInvoce.additionalDiscountRate));
        discountNumber.setText(financeOfInvoce.additionalDiscountSum);
        netoNumber.setText(financeOfInvoce.nettoSum + financeOfInvoce.symbolTotal);
        vatText.setText(processString(vatText.getText(), financeOfInvoce.vatRate));
        vatNumber.setText(financeOfInvoce.vat + financeOfInvoce.symbolTotal);
        payNumber.setText(financeOfInvoce.paySum + financeOfInvoce.symbolTotal);
    }
    
    private String processString(String currState, String newNumberValue){
        String startingPart = currState.substring(0, currState.indexOf(" ") + 1);
        return startingPart + newNumberValue + "%:";
    }
    
    private boolean isEveryNessesaryFieldValid(){
        return  clients.valueProperty().get() != null && 
                !products.nothingIsSelected() && 
                beginDate.getValue() != null && 
                monthCounter.getValue() != null;
    }

    public void bindInvoice(Invoice invoice) {
        this.invoice = invoice;
        if (invoice != null){
            createdDate.setValue(invoice.getCreatedDateObj());
            invoiceNumber.textProperty().bindBidirectional(invoice.invoiceNumberProperty());
            status.textProperty().bindBidirectional(invoice.statusProperty().get().descripProperty());
            
            clients.valueProperty().bindBidirectional(invoice.clientProperty());
            products.setData(invoice.getProductsWithCounts());
            
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
            monthCounter.valueProperty().bindBidirectional(invoice.monthsProperty());
            endDate.valueProperty().bindBidirectional(invoice.endDateProperty());
            
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            licenses.setText(invoice.getLicensesWithDelimiter(", "));
            
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            revokedDate.valueProperty().bindBidirectional(invoice.revokedDateProperty());
            
            processFinanceData(invoice.getInvoiceFinances());
        }
    }
    
    // for test
    private void printMap(Map<Product, Integer> items){
        items.keySet().stream().forEach((p) -> {
            System.out.println("product: " + p.getDescrip() + " count: " + items.get(p));
        });
    }
    
    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        
         // This is Dialog "new" and not add by simple, which EDITOR_BUTTON_TYPE is also NEW.
        if (invoice != null && invoice.getInvoiceFinances().isEmpty()){
            setShowFinanceData(false);
        }
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
