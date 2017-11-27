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
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.Basket;
import ambroafb.general.countcombobox.CountComboBox;
import ambroafb.general.countcombobox.CountComboBoxItem;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentAmount;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.monthcountercombobox.MonthCounterComboBox;
import ambroafb.general.monthcountercombobox.MonthCounterItem;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.InvoiceDataFetchProvider;
import ambroafb.invoices.helper.InvoiceFinance;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceReissuingComboBox;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.helper.LicenseFinance;
import ambroafb.products.ProductDataFetchProvider;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

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
    private InvoiceReissuingComboBox invoiceReissuings;
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
    private RecalcFinancesInBackground financesFromSuitedLicense;
    
    private final String errorMessageBundleKey = "error";
    private Consumer<Exception> commonErrorAction;
    private final ProductDataFetchProvider productFetcher = new ProductDataFetchProvider();

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        clients.fillComboBoxOnlyClients(null);

        financesFromSuitedLicense = new RecalcFinancesInBackground();
        commonErrorAction = (ex) -> {
            String errorHeader = GeneralConfig.getInstance().getTitleFor(errorMessageBundleKey);
            new AlertMessage((Stage)formPane.getScene().getWindow(), Alert.AlertType.ERROR, errorHeader, ex.getLocalizedMessage(), ex).showAndWait();
            System.out.println("<-- in commonErrorAction consumer -->");
        };
        
        setFinanceDataToDefaultText();
    }
    
    private void setFinanceDataToDefaultText(){
        GeneralConfig config = GeneralConfig.getInstance();
        sumText.setText(config.getTitleFor("sum") + colonDelimiter);
        discountText.setText(config.getTitleFor("discount_percent") + percentDelimiter + colonDelimiter);
        netoText.setText(config.getTitleFor("neto") + colonDelimiter);
        vatText.setText(config.getTitleFor("vat_percent") + percentDelimiter + colonDelimiter);
        payText.setText(config.getTitleFor("money_paid") + colonDelimiter);
//        changeFinanceNumbersInfoVisibility(false);
    }
    
    private void changeFinanceNumbersInfoVisibility(boolean visibility){
        changeFinanceInfoVisibility(true, visibility);
    }
    
    private void changeFinanceInfoVisibility(boolean visibleTextData, boolean visibleNumberData){
        financesLabelTextContainer.setVisible(visibleTextData);
        financesLabelNumberContainer.setVisible(visibleNumberData);
    }
    
//    /**
//     * The method starts new finance calculate thread if every required field is filled.
//     * Otherwise sets numeric label to default values.
//     * Starts new thread in every listener, because start thread which 
//     * is already started is incorrect for java.
//     * */
//    @Deprecated
//    private void rebindFinanceData(){
//        if (isEveryNessesaryFieldValid()){
//            new Thread(financesFromSuitedLicense).start();
//        }
//        else {
//            setFinanceDataToDefaultText();
//            changeFinanceInfoVisibility(true, false);
//        }
//    }
    
    /**
     * The method shows invoice finance data on the scene in appropriate labels.
     * @param invoiceFinances The object that contains information on the invoice finance state.
     */
    private void processFinanceData(InvoiceFinance invoiceFinances){
//        changeFinanceNumbersInfoVisibility(!invoiceFinances.dataIsEmpty()); // ---
        if (invoiceFinances.dataIsEmpty()) {
            setFinanceDataToDefaultText();
        } else {
            sumNumber.setText(invoiceFinances.symbolTotal + " " + invoiceFinances.sum);
            discountText.setText(getUpdatedTextWithNumber(discountText.getText(), invoiceFinances.additionalDiscountRate));
            discountNumber.setText(invoiceFinances.symbolTotal + " " + invoiceFinances.additionalDiscountSum);
            netoNumber.setText(invoiceFinances.symbolTotal + " " + invoiceFinances.nettoSum);
            vatText.setText(getUpdatedTextWithNumber(vatText.getText(), invoiceFinances.vatRate));
            vatNumber.setText(invoiceFinances.symbolTotal + " " + invoiceFinances.vat);
            payNumber.setText(invoiceFinances.symbolTotal + " " + invoiceFinances.paySum);
        }
    }
    
    private String getUpdatedTextWithNumber(String currState, String newNumberValue){
        String startingPart = currState.substring(0, currState.indexOf(" ") + 1);
        return startingPart + newNumberValue + percentDelimiter + colonDelimiter;
    }
    
    private boolean isEveryNessesaryFieldValid(){
        return  clients.valueProperty().get() != null && 
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
            beginDate.valueProperty().bindBidirectional(invoice.beginDateProperty());
            monthCounter.valueProperty().bindBidirectional(invoice.monthsProperty());
            endDate.valueProperty().bindBidirectional(invoice.endDateProperty());
            additionalDiscount.textProperty().bindBidirectional(invoice.additionaldiscountProperty());
            licenses.textProperty().bind(invoice.licensesNumbersProperty());
            invoiceReissuings.valueProperty().bindBidirectional(invoice.reissuingProperty());
            revokedDate.valueProperty().bindBidirectional(invoice.revokedDateProperty());
        }
    }
    
    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        Supplier<List<CountComboBoxItem>> productsGenerator = () -> {
                                                                    try {
                                                                        return new ArrayList<>(productFetcher.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL));
                                                                    } catch (Exception ex) {
                                                                    }
                                                                    return null;
                                                                };
        Consumer<ObservableList<CountComboBoxItem>> productsConsumer = (itemsList) -> {
            products.setBasket(convertMapToBasket(((Invoice)sceneObj).getProductsWithCounts()));
        };
        products.fillcomboBox(productsGenerator, productsConsumer);

        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.DELETE)){
            products.setDisabledState(true);
        }
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD)){
            makeActionasForDialogTypeADD();
        }
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            makeActionsForDialogTypeAddBySample();
        }
        processFinanceData(((Invoice)sceneObj).getInvoiceFinance());
        listenToComponentsChange();
    }
    
    private Basket convertMapToBasket(Map<CountComboBoxItem, Integer> data){
        Basket basket = new Basket();
        data.keySet().stream().forEach((boxItem) -> {
            basket.add(boxItem.getUniqueIdentifier(), data.get(boxItem));
        });
        return basket;
    }
    
    private void makeActionasForDialogTypeADD(){
        // Note: We changed objects field but is also change scene field values because of bidirectional binding.
//        ((Invoice)sceneObj).beginDateProperty().set(null); // It is needed for beginDate valuePropety listener, that it does not go to DB for finances.
        Invoice sceneInv = (Invoice)sceneObj;
        Invoice backupInv = (Invoice)backupObj;
        
        System.out.println("before beginDate Change ...");
        sceneInv.beginDateProperty().set(LocalDate.now());
        sceneInv.monthsProperty().set(new MonthCounterItem("1"));
        
        Consumer<ObservableList<InvoiceReissuing>> selectDefaultReissuing = (reissuingList) -> {
            InvoiceReissuing defaultReissuing = reissuingList.stream().filter((reissuing) -> reissuing.getRecId() == InvoiceReissuing.DEFAULT_REISSUING_ID).collect(Collectors.toList()).get(0);
            sceneInv.reissuingProperty().set(defaultReissuing);
            backupInv.reissuingProperty().set(defaultReissuing);
        };
        invoiceReissuings.fillComboBox(selectDefaultReissuing);
        backupObj.copyFrom(sceneObj);
    }
    
    private void makeActionsForDialogTypeAddBySample(){
        makeActionasForDialogTypeADD();
        Invoice sceneInv = (Invoice)sceneObj;
        sceneInv.setInvoiceNumber("");
        sceneInv.getInvoiceStatus().setDescrip(""); // set empty status
        sceneInv.setLicenseFinances(new ArrayList<>()); // empty list cause to clear the products map.
        sceneInv.setInvoiceFinances(new InvoiceFinance());
        products.setBasket(convertMapToBasket(((Invoice)sceneObj).getProductsWithCounts()));
        sceneInv.getLicenses().clear();
        sceneInv.setRevokedDate("");
        backupObj.copyFrom(sceneObj);
    }
    
    /**
     *  The method listens components change that cause to recalculate invoice finance data.
     * It has specific error actions according to changed components. Calculation process will run in separate thread.
     */
    private void listenToComponentsChange(){
        
        Client[] clientsPrevContainer = new Client[1];
        clients.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            System.out.println("new Value: " + newValue);
            if (clientsPrevContainer[0] == null || !clientsPrevContainer[0].equals(newValue)) {
                System.out.println("+++ rebindFinance from clients");
                Consumer<Exception> errorFromClients = (ex) -> {
                    clientsPrevContainer[0] = oldValue;
                    clients.valueProperty().set(oldValue);
                };
                financesFromSuitedLicense.setErrorAction(commonErrorAction.andThen(errorFromClients));
                new Thread(financesFromSuitedLicense).start(); // created new Thread instance in each listener because thread that started once, it can not restart. So it is need to create new instacne for all action.
            }
        });
        
        Basket prevBasket = new Basket();
        products.showingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue){
                prevBasket.clearAndCopy(products.getBasket());
            }
            else if (!prevBasket.equals(products.getBasket())) {
                System.out.println("+++ rebindFinance from products");
                Consumer<Exception> errorFromProducts = (ex) -> {
                    products.setBasket(prevBasket);
                };
                financesFromSuitedLicense.setErrorAction(commonErrorAction.andThen(errorFromProducts));
                new Thread(financesFromSuitedLicense).start();
            }
        });
        
        LocalDate[] beginDatePrevContainer = new LocalDate[1];
        beginDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (beginDatePrevContainer[0] == null || !beginDatePrevContainer[0].equals(newValue)) {
                System.out.println("+++ rebindFinance from beginDate");
                Consumer<Exception> errorFromBeginDate = (ex) -> {
                    beginDatePrevContainer[0] = oldValue;
                    beginDate.setValue(oldValue);
                };
                financesFromSuitedLicense.setErrorAction(commonErrorAction.andThen(errorFromBeginDate));
                new Thread(financesFromSuitedLicense).start();
            }
        });
        monthCounter.valueProperty().addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
            System.out.println("newValue: " + newValue.toString());
            System.out.println("+++ rebindFinance from monthCounter");
            Consumer<Exception> errorFromMonthCounter = (ex) -> {
                monthCounter.setValue(oldValue);
            };
            financesFromSuitedLicense.setErrorAction(commonErrorAction.andThen(errorFromMonthCounter));
            new Thread(financesFromSuitedLicense).start();
        });
        additionalDiscount.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            double discount = Utils.getDoubleValueFor(additionalDiscount.getText());
            if (discount > 0 && !discountText.getText().contains(additionalDiscount.getText()) && !newValue){
                System.out.println("+++ rebindFinance from additionalDiscount");
                financesFromSuitedLicense.setErrorAction(commonErrorAction);
                new Thread(financesFromSuitedLicense).start();
            }
        });
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
        private Consumer<Exception> errorAction;
        
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
        
        public void setErrorAction(Consumer<Exception> errorAction) {
            this.errorAction = errorAction;
        }
        
        private void callSuitedLicenseFromDB(){
            try {
                semLock.acquire();
                Platform.runLater(() -> {
                    masker.setVisible(true);
                });
                
                calculateFinaceData();
                
                Platform.runLater(() -> {
                    processFinanceData(((Invoice)sceneObj).getInvoiceFinance());
                    masker.setVisible(false);
                });
                semLock.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(InvoiceDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void calculateFinaceData() {
            try {
                Consumer<List<PartOfLicense>> licensesConsumer = (List<PartOfLicense> licensesPartDataList) -> {
                    List<Invoice.LicenseShortData> wholeLicenses = licensesPartDataList.stream().map((license) -> {
                        Invoice.LicenseShortData shortData = new Invoice.LicenseShortData();
                        shortData.setLicenseId(license.recId);
                        shortData.setLicenseNumber(license.licenseNumber);
                        if (license.invoiceLicenseId != 0){
                            shortData.setInvoiceLicense(license.invoiceLicenseId);
                        }
                        return shortData;
                    }).collect(Collectors.toList());

                    ((Invoice)sceneObj).setLicenses(wholeLicenses);
                };

                Consumer<List<LicenseFinance>> licenseFinanceConsumer = (List<LicenseFinance> licenseFinances) -> {
                    ((Invoice)sceneObj).setLicenseFinances(licenseFinances);
                };
                
                Consumer<InvoiceFinance> invoiceFianceConsumer = (InvoiceFinance invoiceFinanaceData) -> {
                    ((Invoice)sceneObj).setInvoiceFinances(invoiceFinanaceData);
                };
                DataFetchProvider fetcher = ((InvoiceDialog)formPane.getScene().getWindow()).getDataFetchProvider();
                ((InvoiceDataFetchProvider)fetcher).callInvoiceSuitedLicenses((Invoice)sceneObj, products.getBasket(), licensesConsumer, licenseFinanceConsumer, invoiceFianceConsumer);
//                DBUtils.callInvoiceSuitedLicenses((Invoice)sceneObj, products.getBasket(), licensesConsumer, licenseFinanceConsumer, invoiceFianceConsumer);
            } catch (Exception ex) {
                if (errorAction != null){
                    Platform.runLater(() -> errorAction.accept(ex));
                }
//                JSONObject json = Utils.getJsonFrom(ex.getMessage());
//                if (json == null) return;
//                if (json.optInt("code") == 4020){
//                    String message = processManyProductChoiceException(json.optString("message"));
//                    String title = GeneralConfig.getInstance().getTitleFor("many_product_warning");
//                    Platform.runLater(() -> {
//                        new AlertMessage((Stage)formPane.getScene().getWindow(), Alert.AlertType.WARNING, message, "").showAndWait();
//                    });
//                }
//                else {
//                    Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return;
            }
            
            if (callBack != null){
                callBack.accept(((Invoice)sceneObj));
            }
        }
        
//        private String processManyProductChoiceException(String exceptionMessage) {
//            String msg = "";
//            StringTokenizer tok = new StringTokenizer(exceptionMessage, ",");
//            while (tok.hasMoreTokens()) {
//                String currErrorText = tok.nextToken();
//                String prodId = StringUtils.substringBetween(currErrorText, "product:", "count:").trim();
//                String prodCurrCount = StringUtils.substringBetween(currErrorText, "count:", "max:").trim();
//                String prodMaxCount = StringUtils.substringAfter(currErrorText, "max:").trim();
//                Product appProduct = (Product)((Invoice)sceneObj).getProductsWithCounts().keySet().stream().filter((CountComboBoxItem p) -> ((Product)p).getRecId() == Integer.parseInt(prodId)).collect(Collectors.toList()).get(0);
//                if (appProduct != null) {
//                    msg += appProduct.getDescrip() + " -> " + GeneralConfig.getInstance().getTitleFor("max_count") + ": " + prodMaxCount + "\n";
//                }
//            }
//            return msg;
//        }
    }
}
