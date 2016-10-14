/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.balance_accounts.BalanceAccounts;
import ambroafb.clients.Clients;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.countries.Countries;
import ambroafb.currencies.Currencies;
import ambroafb.currency_rates.CurrencyRates;
import ambroafb.currency_rates.filter.CurrencyRateFilter;
import ambroafb.discounts_on_count.DiscountOnCounts;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.invoices.Invoices;
import ambroafb.invoices.filter.InvoiceFilter;
import ambroafb.licenses.Licenses;
import ambroafb.licenses.filter.LicenseFilter;
import ambroafb.products.Products;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class MainController implements Initializable {
    
    private GeneralConfig config;
    
    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane formPane;
    @FXML
    private Button back;
    
    @FXML
    private void light(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/light/Light.fxml", config.getTitleFor("light"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION,  "Light");
            alert.showAlert();
        }
    }
    @FXML
    private void autoDealers(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/auto_dealers/AutoDealers.fxml", config.getTitleFor("auto_dealers"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION, "Auto Dealers");
            alert.showAlert();
        }
    }
    
    @FXML //დროებით აღარ ვიყენებ, მენიუს პუნქტიდან ამოვიღეთ და ჩავსვამთ ანგარიშთა სიაში ერთ-ერთ პიქტოგრამად
    private void newAccount(ActionEvent event) {
        try{
            Stage stage = Utils.createStage("/ambroafb/new_account/NewAccount.fxml", config.getTitleFor("open_new_account"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION, "Account");
            alert.showAlert();
        }
    }
    
    @FXML
    private void mainConfig(ActionEvent event) {
        try{
            Stage stage = Utils.createStage(
                    Names.CONFIGURATION_FXML, 
                    config.getTitleFor(Names.CONFIGURATION_TITLE), 
                    Names.CONFIGURATION_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        } catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION, "Configuration");
            alert.showAlert();
        }
    }
    
    @FXML
    public void mainExit(ActionEvent event){
        StagesContainer.saveSizeFor(AmbroAFB.mainStage);
        if (StagesContainer.closeStageWithChildren(AmbroAFB.mainStage)){
            Utils.exit();
        }
    }
    
    @FXML
    private void programsCarFines(ActionEvent event) {
        try{
            Stage stage = Utils.createMultiSceneStage(
                    Names.CAR_FINES_FXML, 
                    config.getTitleFor(Names.CAR_FINES_TITLE), 
                    Names.CAR_FINES_LOGO,
                    AmbroAFB.mainStage
            );
            stage.show();
        }catch(IOException ex){
            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_CAR_FINES_SCENE_START, "Car Fines");
            alert.showAlert();
        }
    }
    
    @FXML
    private void clients(ActionEvent event) {
        String clientsStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Clients.class.getSimpleName();
        
        Stage clientsStage = StagesContainer.getStageForPath(clientsStagePath);
        if(clientsStage == null || !clientsStage.isShowing()){
            Clients clients = new Clients(AmbroAFB.mainStage);
            clients.show();
            
            ClientFilter filter = new ClientFilter(clients);
            JSONObject json = filter.getResult();
            clients.getClientsController().reAssignTable(json);

            if (json != null && json.length() == 0) 
                clients.close();
        }
        else {
            clientsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, clientsStage);
        }
    }
    
    @FXML
    private void loggings(ActionEvent event){
//        String loggingsStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Loggings.class.getSimpleName();
//        
//        Stage loggings = StagesContainer.getStageForPath(loggingsStagePath);
//        if(loggings == null || !loggings.isShowing()){
//            Loggings loggings = new Loggings(AmbroAFB.mainStage);
//            loggings.show();
//            
//            LoggingFilter filter = new LoggingFilter(loggings);
//            JSONObject json = filter.getResult();
//            loggings.getLoggingsController().reAssignTable(json);
//
//            if (json != null && json.length() == 0) 
//                loggings.close();
//        }
//        else {
//            loggings.requestFocus();
//        }
    }
    
    @FXML 
    private void invoices(ActionEvent event) {
        String invoicesStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Invoices.class.getSimpleName();
        
        Stage invoicesStage = StagesContainer.getStageForPath(invoicesStagePath);
        if(invoicesStage == null || !invoicesStage.isShowing()){
            Invoices invoices = new Invoices(AmbroAFB.mainStage);
            invoices.show();
            
            InvoiceFilter filter = new InvoiceFilter(invoices);
            invoices.getInvoicesController().reAssignTable(filter.getResult());
        }
        else {
            invoicesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, invoicesStage);
        }
    }
    
    @FXML 
    private void products(ActionEvent event) {
        String productsStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Products.class.getSimpleName();
        Stage productsStage = StagesContainer.getStageForPath(productsStagePath);
        if (productsStage == null || !productsStage.isShowing()){
            Products products = new Products(AmbroAFB.mainStage);
            products.show();
        }
        else{
            productsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, productsStage);
        }
    }
    
    @FXML 
    private void countries(ActionEvent event) {
        String countriesStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Countries.class.getSimpleName();
        
        Stage countriesStage = StagesContainer.getStageForPath(countriesStagePath);
        if (countriesStage == null || !countriesStage.isShowing()){
            Countries countries = new Countries(AmbroAFB.mainStage);
            countries.show();
        }
        else {
            countriesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, countriesStage);
        }
    }
    
    
    
    @FXML private void accounts(ActionEvent event) {}
    
    @FXML private void licenses(ActionEvent event) {
        String licensesStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + Licenses.class.getSimpleName();
        
        Stage licensesStage = StagesContainer.getStageForPath(licensesStagePath);
        if(licensesStage == null || !licensesStage.isShowing()){
            Licenses licenses = new Licenses(AmbroAFB.mainStage);
            licenses.show();
            
            LicenseFilter filter = new LicenseFilter(licenses);
            JSONObject json = filter.getResult();
            licenses.getLicensesController().reAssignTable(json);

            if (json != null && json.length() == 0) 
                licenses.close();
        }
        else {
            licensesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, licensesStage);
        }
    }
    
    @FXML private void clientStatuses(ActionEvent event) {}
    @FXML private void licenseByInvoiceCovers(ActionEvent event) {}
    @FXML private void invoice_reissuings(ActionEvent event) {}
    @FXML private void productSpecifics(ActionEvent event) {}
    @FXML private void tm(ActionEvent event) {}
    
    @FXML private void currencies(ActionEvent event) {
        String currenciesStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + CurrencyRates.class.getSimpleName();
        
        Stage currenciesStage = StagesContainer.getStageForPath(currenciesStagePath);
        if(currenciesStage == null || !currenciesStage.isShowing()){
            Currencies currencies = new Currencies(AmbroAFB.mainStage);
            currencies.show();
        }
        else {
            currenciesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currenciesStage);
        }
    }
    
    @FXML private void currencyRates(ActionEvent event) {
        String currencyRatesStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + CurrencyRates.class.getSimpleName();
        
        Stage currencyRatesStage = StagesContainer.getStageForPath(currencyRatesStagePath);
        if(currencyRatesStage == null || !currencyRatesStage.isShowing()){
            CurrencyRates currencyRates = new CurrencyRates(AmbroAFB.mainStage);
            currencyRates.show();
            
            CurrencyRateFilter filter = new CurrencyRateFilter(currencyRates);
            JSONObject json = filter.getResult();
            currencyRates.getCurrencyRatesController().reAssignTable(json);

            if (json != null && json.length() == 0) 
                currencyRates.close();
        }
        else {
            currencyRatesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currencyRatesStage);
        }
    }
    
    @FXML private void discountsOnCount(ActionEvent event) {
        String discountOnCountsStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + DiscountOnCounts.class.getSimpleName();
        
        Stage discountOnCountsStage = StagesContainer.getStageForPath(discountOnCountsStagePath);
        if (discountOnCountsStage == null || !discountOnCountsStage.isShowing()){
            DiscountOnCounts discountOnCounts = new DiscountOnCounts(AmbroAFB.mainStage);
            discountOnCounts.show();
        }
        else {
            discountOnCountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, discountOnCountsStage);
        }
    }
    
    @FXML private void balAccounts(ActionEvent event) {
        String balAccountsStagePath = StagesContainer.getPathForStage(AmbroAFB.mainStage) + "/" + BalanceAccounts.class.getSimpleName();
        
        Stage balAccountsStage = StagesContainer.getStageForPath(balAccountsStagePath);
        if (balAccountsStage == null || !balAccountsStage.isShowing()){
            BalanceAccounts accounts = new BalanceAccounts(AmbroAFB.mainStage);
            accounts.show();
        }
        else {
            balAccountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, balAccountsStage);
        }
    }
    
    @FXML private void balances(ActionEvent event) {}
    @FXML private void account_statments(ActionEvent event) {}
    @FXML private void other(ActionEvent event) {}
    
//    @FXML
//    private void programsInOut(ActionEvent event) {
//        try{
//            Stage stage = Utils.createStage(
//                    Names.IN_OUT_FXML, 
//                    config.getTitleFor(Names.IN_OUT_TITLE), 
//                    Names.IN_OUT_LOGO,
//                    AmbroAFB.mainStage
//            );
//            stage.show();
//        }catch(IOException ex){
////            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
////            alert.showAlert();
//           
//            Platform.runLater(() -> {
//                AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_IN_OUT_START_SCENE);
//                alert.showAlert();
//                System.out.println("errorr after");
//            });
//        }
//    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = GeneralConfig.getInstance();
        menuBar.prefWidthProperty ().bind (AmbroAFB.mainStage.widthProperty ());
    }        
}
