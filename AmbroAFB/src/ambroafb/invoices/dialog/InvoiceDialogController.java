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
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBox;
import ambroafb.general.countcombobox.CountComboBoxItem;
import ambroafb.general.interfaces.Annotations.ContentAmount;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.monthcountercombobox.MonthCounterComboBox;
import ambroafb.general.monthcountercombobox.MonthCounterItem;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceFinaces;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.helper.LicenseFinaces;
import ambroafb.products.Product;
import authclient.AuthServerException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.MaskerPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class InvoiceDialogController extends DialogController {

    // The order is needed for required components show ordering on scene:
    // start order:
    @FXML @ContentNotEmpty
    private ClientComboBox clients;
    @FXML @ContentNotEmpty
    private CountComboBox products;
    @FXML @ContentNotEmpty
    private ADatePicker beginDate;
    @FXML @ContentNotEmpty
    private MonthCounterComboBox monthCounter;
    @FXML @ContentAmount
    private AmountField additionalDiscount;
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
    
    
    private String colonDelimiter = ":";
    private String percentDelimiter = "%";
    private Names.EDITOR_BUTTON_TYPE editorPanelButtonType;
    private Runnable financesFromSuitedLicense;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
//        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        
        clients.fillComboBoxOnlyClients(null);
        products.getItems().addAll(Product.getAllFromDB());
//        permissionToClose = true;

        financesFromSuitedLicense = new RecalcFinancesInBackground();
        clients.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebindFinance from clients");
                rebindFinanceData();
            }
        });
        products.showingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (oldValue != null && !newValue && !Utils.compareProductsCounter(((Invoice)sceneObj).getProductsWithCounts(), ((Invoice)backupObj).getProductsWithCounts())) {
                System.out.println("rebindFinance from products");
                rebindFinanceData();
            }
        });
        beginDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebindFinance from beginDate");
                rebindFinanceData();
            }
        });
        monthCounter.valueProperty().addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
            if (oldValue != null && newValue != null){
                System.out.println("rebindFinance from monthCounter");
                rebindFinanceData();
            }
        });
        additionalDiscount.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            double discount = Utils.getDoubleValueFor(additionalDiscount.getText());
            if (discount > 0 && !discountText.getText().contains(additionalDiscount.getText()) && !newValue){
                System.out.println("rebindFinance from additionalDiscount");
                rebindFinanceData();
            }
        });
        
        setFinanceDataToDefaultText();
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

    public CountComboBox getProductsComboBox(){
        return products;
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }
    

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Invoice invoice = (Invoice)object;
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
            
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            revokedDate.valueProperty().bindBidirectional(invoice.revokedDateProperty());
            
            processFinanceData(invoice.getInvoiceFinances());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanelable sceneObject, Names.EDITOR_BUTTON_TYPE buttonType) {
        editorPanelButtonType = buttonType;
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            products.changeState(true);
        }
        
        Invoice invoice = (Invoice)sceneObject;
        // This is Dialog "new" and not add by simple, which EDITOR_BUTTON_TYPE is also NEW.
        if (invoice != null && invoice.getInvoiceFinances().isEmpty()){
            setShowFinanceData(true, false);
        }
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            // Note: We changed objects field but is also change scene field values because of bidirectional binding.
            invoice.beginDateProperty().set(null); // It is needed for beginDate valuePropety listener, that it does not go to DB for finances.
            invoice.beginDateProperty().set(LocalDate.now());
            ((Invoice)backupObj).beginDateProperty().set(LocalDate.now());
            
            InvoiceReissuing defaultReissuing = invoiceReissuings.getItems().stream().filter((reissuing) -> reissuing.getRecId() == InvoiceReissuing.DEFAULT_REISSUING_ID).collect(Collectors.toList()).get(0);
            invoice.reissuingProperty().set(defaultReissuing);
            ((Invoice)backupObj).reissuingProperty().set(defaultReissuing);
            
            if (clients.getValue() != null){ // add by simple
                invoice.setInvoiceNumber("");
                invoice.getInvoiceStatus().setDescrip(""); // set empty status
                invoice.getLicenses().clear();
                
                Consumer<Invoice> updateInvoiceBackup = (Invoice inv) -> {
                    ((Invoice)backupObj).copyFrom(inv);
                };
                if (isEveryNessesaryFieldValid()){
                    // we need new license numbers:
                    new Thread(new RecalcFinancesInBackground(updateInvoiceBackup)).start();
                }
            }
        }
    }
    
    @Override
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
                    processFinanceData(((Invoice)sceneObj).getInvoiceFinances());
                    masker.setVisible(false);
                });
                semLock.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(InvoiceDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void calculateFinaceData() {
            setShowFinanceData(true, true);
            Map<CountComboBoxItem, Integer> productsMap = products.getData();
            
//            System.out.println("calculateFinaceData -> productsMap: " + productsMap);

            JSONArray productsArray = new JSONArray();
            productsMap.keySet().stream().forEach((item) -> {
                Product product = (Product)item;
                JSONObject json = Utils.getJsonFrom(null, "product_id", product.getRecId());
                productsArray.put(Utils.getJsonFrom(json, "count", productsMap.get(product)));
            });
//            System.out.println("rebindFinanceData -> productsArray minda: " + productsArray);

            Integer invoiceId = null;
            if (((Invoice)sceneObj).getRecId() != 0 && !editorPanelButtonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
                invoiceId = ((Invoice)sceneObj).getRecId();
            }
            
            Float discount = ((Invoice)sceneObj).getAdditionalDiscountRate();
//            String discount = invoice.getAdditionalDiscountRate();
//            if (discount == null || discount.isEmpty()) {
//                discount = null;
//            }
            
            JSONArray licensesIds = new JSONArray();
            ((Invoice)sceneObj).getLicenses().forEach((licenseShortData) -> {
                licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.getLicense_id()));
            });
//            System.out.println("maqvs  license_ids: " + licensesIds);

            try {
                DBUtils.callInvoiceSuitedLicenses(invoiceId, ((Invoice)sceneObj).getClientId(), ((Invoice)sceneObj).beginDateProperty().get(), ((Invoice)sceneObj).endDateProperty().get(), productsArray, discount, licensesIds);
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

            ((Invoice)sceneObj).setLicenseFinances(licenseFinances);
            ((Invoice)sceneObj).setInvoiceFinances(invoiceFinances);
            List<Invoice.LicenseShortData> wholeLicenses = invoiceLicenses.stream().map((license) -> {
                Invoice.LicenseShortData shortData = new Invoice.LicenseShortData();
                shortData.setLicenseId(license.recId);
                shortData.setLicenseNumber(license.licenseNumber);
                if (license.invoiceLicenseId != 0){
                    shortData.setInvoiceLicense(license.invoiceLicenseId);
                }
                return shortData;
            }).collect(Collectors.toList());
            
            ((Invoice)sceneObj).setLicenses(wholeLicenses);
            
//            System.out.println("invoice new licenses: " + invoice.getLicenses().toString());
            
            if (callBack != null){
                callBack.accept(((Invoice)sceneObj));
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
                Product appProduct = (Product)((Invoice)sceneObj).getProductsWithCounts().keySet().stream().filter((CountComboBoxItem p) -> ((Product)p).getRecId() == Integer.parseInt(prodId)).collect(Collectors.toList()).get(0);
                if (appProduct != null) {
                    msg += appProduct.getDescrip() + " -> " + GeneralConfig.getInstance().getTitleFor("max_count") + ": " + prodMaxCount + "\n";
                }
            }
            return msg;
        }
    }
}
