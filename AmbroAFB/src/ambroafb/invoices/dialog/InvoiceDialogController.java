/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.AlertMessage;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
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
import authclient.AuthServerException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.MaskerPane;
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
    @FXML @ContentPattern(value = "^$|(([1-9]\\d*)?\\d)(\\.\\d\\d)?$", explain = "The discount syntax is incorrect. like: 0.35, 3.25 ...")
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
    private MaskerPane masker;
    @FXML
    private Label   sumText, sumNumber, discountText, discountNumber, 
                    netoText, netoNumber, vatText, vatNumber, payText, payNumber;
    
    
    private ArrayList<Node> focusTraversableNodes;
    private Invoice invoice;
    private Invoice invoiceBackup;
    private boolean permissionToClose;
    private String colonDelimiter = ":";
    private String percentDelimiter = "%";
    private Names.EDITOR_BUTTON_TYPE editorPanelButtonType;
    private Runnable financesFromSuitedLicense;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        processInvoiceReissuingComboBox();
        clients.showCategoryALL(false);
        products.getItems().addAll(Product.getAllFromDB());
        permissionToClose = true;

        financesFromSuitedLicense = new RecalcFinancesInBackground();
        clients.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebind from clients");
                rebindFinanceData();
            }
        });
        products.showingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue != null && !newValue && !Utils.compareProductsCounter(invoice.getProductsWithCounts(), invoiceBackup.getProductsWithCounts())) {
                System.out.println("rebind from products");
                rebindFinanceData();
            }
        });
        beginDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebind from beginDate");
                rebindFinanceData();
            }
        });
        monthCounter.valueProperty().addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebind from monthCounter");
                rebindFinanceData();
            }
        });
        additionalDiscount.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            double discount = Utils.getDoubleValueFor(additionalDiscount.getText());
            if (discount > 0 && !newValue){
                System.out.println("rebind from additionalDiscount");
                rebindFinanceData();
            }
        });
        
        setFinanceDataToDefaultText();
    }
    
    private void processInvoiceReissuingComboBox(){
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        InvoiceReissuing defaultReissuing = invoiceReissuings.getItems().stream().filter((reissuing) -> reissuing.getRecId() == InvoiceReissuing.DEFAULT_REISSUING_ID).collect(Collectors.toList()).get(0);
        invoiceReissuings.setValue(defaultReissuing);
    }
    
    private void setFinanceDataToDefaultText(){
        GeneralConfig config = GeneralConfig.getInstance();
        sumText.setText(config.getTitleFor("sum") + colonDelimiter);
        discountText.setText(config.getTitleFor("discount_percent") + percentDelimiter + colonDelimiter);
        netoText.setText(config.getTitleFor("neto") + colonDelimiter);
        vatText.setText(config.getTitleFor("vat_percent") + percentDelimiter + colonDelimiter);
        payText.setText(config.getTitleFor("money_paid") + colonDelimiter);
    }
    
    private void setShowFinanceData(boolean showTextLabel, boolean showNumberLabel){
        financesLabelTextContainer.setVisible(showTextLabel);
        financesLabelNumberContainer.setVisible(showNumberLabel);
    }
    
    /**
     * The method starts new finance calculate thread if every required field is filled.
     * Otherwise sets numeric label to default values.
     * Starts new thread in every listener, because start thread which 
     * is already started is incorrect for java.
     * */
    private void rebindFinanceData(){
        if (isEveryNessesaryFieldValid()){
            new Thread(financesFromSuitedLicense).start();
        }
        else {
            setFinanceDataToDefaultText();
            setShowFinanceData(true, false);
        }
    }
    
    private void processFinanceData(ArrayList<InvoiceFinaces> invoiceFinances){
        if (invoiceFinances.isEmpty()) return;
        InvoiceFinaces financeOfInvoce = invoiceFinances.get(0);
        sumNumber.setText(financeOfInvoce.symbolTotal + " " + financeOfInvoce.sum);
        discountText.setText(getUpdatedTextWithNumber(discountText.getText(), financeOfInvoce.additionalDiscountRate));
        discountNumber.setText(financeOfInvoce.symbolTotal + " " + financeOfInvoce.additionalDiscountSum);
        netoNumber.setText(financeOfInvoce.symbolTotal + " " + financeOfInvoce.nettoSum);
        vatText.setText(getUpdatedTextWithNumber(vatText.getText(), financeOfInvoce.vatRate));
        vatNumber.setText(financeOfInvoce.symbolTotal + " " + financeOfInvoce.vat);
        payNumber.setText(financeOfInvoce.symbolTotal + " " + financeOfInvoce.paySum);
    }
    
    private String getUpdatedTextWithNumber(String currState, String newNumberValue){
        String startingPart = currState.substring(0, currState.indexOf(" ") + 1);
        return startingPart + newNumberValue + percentDelimiter + colonDelimiter;
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
            status.textProperty().bindBidirectional(invoice.getInvoiceStatus().descripProperty());
            
            clients.valueProperty().bindBidirectional(invoice.clientProperty());
            products.setData(invoice.getProductsWithCounts());
            
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
            monthCounter.valueProperty().bindBidirectional(invoice.monthsProperty());
            endDate.valueProperty().bindBidirectional(invoice.endDateProperty());
            
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            licenses.textProperty().bind(invoice.licensesNumbersProperty());
            
            if (invoice.reissuingProperty().get() != null){
                invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            }
            revokedDate.valueProperty().bindBidirectional(invoice.revokedDateProperty());
            
            processFinanceData(invoice.getInvoiceFinances());
        }
    }
    
    public ComboBox<Product> getProductsComboBox(){
        return products;
    }
    
    // for test
    private void printMap(Map<Product, Integer> items){
        items.keySet().stream().forEach((p) -> {
            System.out.println("product: " + p.getDescrip() + " count: " + items.get(p));
        });
    }
    
    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        editorPanelButtonType = buttonType;
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        
        // This is Dialog "new" and not add by simple, which EDITOR_BUTTON_TYPE is also NEW.
        if (invoice != null && invoice.getInvoiceFinances().isEmpty()){
            setShowFinanceData(true, false);
        }
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            // Note: We changed objects field but is also change scene field values because of bidirectional binding.
            invoice.beginDateProperty().set(null); // It is needed for beginDate valuePropety listener, that it does not go to DB for finances.
            invoice.beginDateProperty().set(LocalDate.now());
            invoiceBackup.beginDateProperty().set(LocalDate.now());
            
            if (clients.getValue() != null){ // add by simple
                invoice.setInvoiceNumber("");
                invoice.getInvoiceStatus().setDescrip(""); // set empty status
                
                Consumer<Invoice> updateInvoiceBackup = (Invoice invoice1) -> {
                    invoiceBackup.copyFrom(invoice1);
                };
                if (isEveryNessesaryFieldValid()){
                    // we need new license numbers:
                    new Thread(new RecalcFinancesInBackground(updateInvoiceBackup)).start();
                }
            }
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
    
    
    /**
     * The class provides to compute fiance data into run method.
     * The class uses semaphore for calculate operation by atomic. It updates UI component into 
     * Platform thread. The MaskerPane visible, while finances data is not new.
     */
    private class RecalcFinancesInBackground implements Runnable {

        private final Semaphore semLock;
        private Consumer<Invoice> callBack;
        
        public RecalcFinancesInBackground(Consumer<Invoice> callBack){
            this();
            this.callBack = callBack;
        }
        
        public RecalcFinancesInBackground(){
            semLock = new Semaphore(1);
        }
        
        @Override
        public void run() {
            callSuitedLicenseFromDB();
        }
        
        private void callSuitedLicenseFromDB(){
            try {
                semLock.acquire();
                Platform.runLater(() -> {
                    masker.setVisible(true);
                });
                
                calculateFinaceData();
                
                Platform.runLater(() -> {
                    processFinanceData(invoice.getInvoiceFinances());
                    masker.setVisible(false);
                });
                semLock.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(InvoiceDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void calculateFinaceData() {
            setShowFinanceData(true, true);
            Map<Product, Integer> productsMap = products.getData();
            JSONArray productsArray = new JSONArray();
            productsMap.keySet().stream().forEach((product) -> {
                JSONObject json = Utils.getJsonFrom(null, "product_id", product.getRecId());
                productsArray.put(Utils.getJsonFrom(json, "count", productsMap.get(product)));
            });
            System.out.println("rebindFinanceData -> productsArray: " + productsArray);

            Integer invoiceId = null;
            if (invoice.getRecId() != 0 && !editorPanelButtonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
                invoiceId = invoice.getRecId();
            }
            
            String discount = invoice.getAdditionalDiscountRate();
            if (discount == null || discount.isEmpty()) {
                discount = null;
            }
            
            JSONArray licensesIds = new JSONArray();
            if (invoiceId != null){ // If invoice is new and have licenses (Add-By-Sample), their licensesIds don't sent to DB.
                invoice.getLicenses().forEach((licenseShortData) -> {
                    licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.getLicense_id()));
                });
            }

            try {
                DBUtils.callInvoiceSuitedLicenses(invoiceId, invoice.getClientId(), invoice.beginDateProperty().get(), invoice.endDateProperty().get(), productsArray, discount, licensesIds);
            } catch (AuthServerException ex) {
                JSONObject json = Utils.getJsonFrom(ex.getMessage());
                if (json == null) return;
                if (json.optInt("code") == 4020){
                    String message = processManyProductChoiceException(json.optString("message"));
                    String title = GeneralConfig.getInstance().getTitleFor("many_product_warning");
                    Platform.runLater(() -> {
                        new AlertMessage(Alert.AlertType.WARNING, ex, message, title).showAlert();
                    });
                }
                else {
                    Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            
            ArrayList<PartOfLicense> invoiceLicenses = DBUtils.getLicenses();
            ArrayList<LicenseFinaces> licenseFinances = DBUtils.getLicensesFinaces();
            ArrayList<InvoiceFinaces> invoiceFinances = DBUtils.getInvoicesFinaces();

            invoice.setLicenseFinances(licenseFinances);
            invoice.setInvoiceFinances(invoiceFinances);
            List<Invoice.LicenseShortData> wholeLicenses = invoiceLicenses.stream().map((license) -> {
                Invoice.LicenseShortData shortData = new Invoice.LicenseShortData();
                shortData.setLicenseId(license.recId);
                shortData.setLicenseNumber(license.licenseNumber);
                if (license.invoiceLicenseId != 0){
                    shortData.setInvoiceLicense(license.invoiceLicenseId);
                }
                return shortData;
            }).collect(Collectors.toList());
            
            invoice.setLicenses(wholeLicenses);
            if (callBack != null){
                callBack.accept(invoice);
            }
        }
        
        private String processManyProductChoiceException(String exceptionMessage) {
            String msg = "";
            StringTokenizer tok = new StringTokenizer(exceptionMessage, ",");
            while (tok.hasMoreTokens()) {
                String currErrorText = tok.nextToken();
                String prodId = StringUtils.substringBetween(currErrorText, "product:", "count:").trim();
                String prodCurrCount = StringUtils.substringBetween(currErrorText, "count:", "max:").trim();
                String prodMaxCount = StringUtils.substringAfter(currErrorText, "max:").trim();
                Product appProduct = invoice.getProductsWithCounts().keySet().stream().filter((Product p) -> p.getRecId() == Integer.parseInt(prodId)).collect(Collectors.toList()).get(0);
                if (appProduct != null) {
                    msg += appProduct.getDescrip() + GeneralConfig.getInstance().getTitleFor("max_count") + prodMaxCount + "\n";
                }
            }
            return msg;
        }
    }
}
