/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.balance_accounts.BalanceAccounts;
import ambroafb.clients.Clients;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.configuration.Configuration;
import ambroafb.countries.Countries;
import ambroafb.currencies.Currencies;
import ambroafb.currency_rates.CurrencyRates;
import ambroafb.currency_rates.filter.CurrencyRateFilter;
import ambroafb.discounts_on_count.DiscountOnCounts;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.invoices.Invoices;
import ambroafb.invoices.filter.InvoiceFilter;
import ambroafb.licenses.Licenses;
import ambroafb.licenses.filter.LicenseFilter;
import ambroafb.loggings.Loggings;
import ambroafb.loggings.filter.LoggingFilter;
import ambroafb.minitables.MiniTables;
import ambroafb.minitables.buysells.BuySell;
import ambroafb.minitables.permanences.Permanence;
import ambroafb.minitables.subjects.Subject;
import ambroafb.products.Products;
import authclient.AuthServerException;
import authclient.monitoring.MonitoringClient;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class MainController implements Initializable {
    
    @FXML
    private BorderPane formPane;
    @FXML
    private HBox menusPane;
    
    @FXML
    private void light(ActionEvent event) {
//        try{
//            Stage stage = Utils.createStage("/ambroafb/light/Light.fxml", config.getTitleFor("light"), "/images/innerLogo.png", AmbroAFB.mainStage);
//            stage.show();
//        } catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION,  "Light");
//            alert.showAlert();
//        }
    }
    @FXML
    private void autoDealers(ActionEvent event) {
//        try{
//            Stage stage = Utils.createStage("/ambroafb/auto_dealers/AutoDealers.fxml", config.getTitleFor("auto_dealers"), "/images/innerLogo.png", AmbroAFB.mainStage);
//            stage.setResizable(false);
//            stage.show();
//        } catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION, "Auto Dealers");
//            alert.showAlert();
//        }
    }
    
    @FXML //დროებით აღარ ვიყენებ, მენიუს პუნქტიდან ამოვიღეთ და ჩავსვამთ ანგარიშთა სიაში ერთ-ერთ პიქტოგრამად
    private void newAccount(ActionEvent event) {
//        try{
//            Stage stage = Utils.createStage("/ambroafb/new_account/NewAccount.fxml", config.getTitleFor("open_new_account"), "/images/innerLogo.png", AmbroAFB.mainStage);
//            stage.setResizable(false);
//            stage.show();
//        } catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION, "Account");
//            alert.showAlert();
//        }
    }
    
    @FXML
    private void mainConfig(ActionEvent event) {
        Stage configStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Configuration.class.getSimpleName());
        
        if (configStage == null  || !configStage.isShowing()){
            Configuration configuration = new Configuration(AmbroAFB.mainStage);
            configuration.show();
        }
        else {
            configStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, configStage);
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
//        try{
//            Stage stage = Utils.createMultiSceneStage(
//                    Names.CAR_FINES_FXML, 
//                    config.getTitleFor(Names.CAR_FINES_TITLE), 
//                    Names.CAR_FINES_LOGO,
//                    AmbroAFB.mainStage
//            );
//            stage.show();
//        }catch(IOException ex){
//            AlertMessage alert = new AlertMessage(AlertType.ERROR, ex, Names.ERROR_CAR_FINES_SCENE_START, "Car Fines");
//            alert.showAlert();
//        }
    }
    
    @FXML
    private void clients(ActionEvent event) {
        Stage clientsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Clients.class.getSimpleName());
        if(clientsStage == null || !clientsStage.isShowing()){
            Clients clients = new Clients(AmbroAFB.mainStage);
            clients.show();
            
            ClientFilter filter = new ClientFilter(clients);
            FilterModel model = filter.getResult();
            clients.getClientsController().reAssignTable(model);
            
            if (model.isCanceled()){
                clients.close();
            }
        }
        else {
            clientsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, clientsStage);
        }
    }
    
    @FXML
    private void loggings(ActionEvent event){
        Stage loggingsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Loggings.class.getSimpleName());
        
        if(loggingsStage == null || !loggingsStage.isShowing()){
            Loggings loggings = new Loggings(AmbroAFB.mainStage);
            loggings.show();
            
            LoggingFilter filter = new LoggingFilter(loggings);
            FilterModel model = filter.getResult();
            loggings.getLoggingsController().reAssignTable(model);

            if (model.isCanceled()) 
                loggings.close();
        }
        else {
            loggingsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, loggingsStage);
        }
    }
    
    @FXML 
    private void invoices(ActionEvent event) {
        Stage invoicesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Invoices.class.getSimpleName());
        if(invoicesStage == null || !invoicesStage.isShowing()){
            Invoices invoices = new Invoices(AmbroAFB.mainStage);
            invoices.show();
            
            InvoiceFilter filter = new InvoiceFilter(invoices);
            FilterModel model = filter.getResult();
            invoices.getInvoicesController().reAssignTable(model);
            
            if (model.isCanceled()){
                invoices.close();
            }
        }
        else {
            invoicesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, invoicesStage);
        }
    }
    
    @FXML 
    private void products(ActionEvent event) {
        Stage productsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Products.class.getSimpleName());
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
        Stage countriesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Countries.class.getSimpleName());
        if (countriesStage == null || !countriesStage.isShowing()){
            Countries countries = new Countries(AmbroAFB.mainStage);
            countries.show();
        }
        else {
            countriesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, countriesStage);
        }
        
        System.out.println("formPane: " + formPane.getWidth());
        System.out.println("menusPane: " + menusPane.getWidth());
    }
    
    
    
    @FXML private void accounts(ActionEvent event) {}
    
    @FXML private void licenses(ActionEvent event) {
        Stage licensesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Licenses.class.getSimpleName());
        if(licensesStage == null || !licensesStage.isShowing()){
            Licenses licenses = new Licenses(AmbroAFB.mainStage);
            licenses.show();
            
            LicenseFilter filter = new LicenseFilter(licenses);
            FilterModel filterModel = filter.getResult();
            licenses.getLicensesController().reAssignTable(filterModel);

            if (filterModel.isCanceled()) 
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
    
    
    @FXML
    private void buysells(ActionEvent event){
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, MiniTables.class.getSimpleName());
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            try {
                Class contentClass = Class.forName("ambroafb.minitables.buysells.BuySell");
                ArrayList<BuySell> buySellList = BuySell.getAllFromDB();
                buySellList.sort((BuySell b1, BuySell b2) -> b1.getDescrip().compareTo(b2.getDescrip()));
                String stageLocalizableTitle = GeneralConfig.getInstance().getTitleFor("buysells");
                
                showMiniTablesStage(contentClass, buySellList, stageLocalizableTitle);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    @FXML
    private void permanences(ActionEvent event){
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, MiniTables.class.getSimpleName());
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            try {
                Class contentClass = Class.forName("ambroafb.minitables.permanences.Permanence");
                ArrayList<Permanence> permanenceList = Permanence.getAllFromDB();
                permanenceList.sort((Permanence p1, Permanence p2) -> p1.getDescrip().compareTo(p2.getDescrip()));
                String stageLocalizableTitle = GeneralConfig.getInstance().getTitleFor("permanences");
                
                showMiniTablesStage(contentClass, permanenceList, stageLocalizableTitle);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    @FXML
    private void subjects(ActionEvent event){
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, MiniTables.class.getSimpleName());
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            try {
                Class contentClass = Class.forName("ambroafb.minitables.subjects.Subject");
                ArrayList<Subject> subjectList = Subject.getAllFromDB();
                subjectList.sort((Subject s1, Subject s2) -> s1.getDescrip().compareTo(s2.getDescrip()));
                String stageLocalizableTitle = GeneralConfig.getInstance().getTitleFor("subjects");
                
                showMiniTablesStage(contentClass, subjectList, stageLocalizableTitle);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
        
    private void showMiniTablesStage(Class contentClass, ArrayList<? extends EditorPanelable> list, String stageLocalizableTitle){
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, contentClass.getSimpleName());
        if (miniTablesStage == null || !miniTablesStage.isShowing()) {
            MiniTables miniTables = new MiniTables(AmbroAFB.mainStage, contentClass, stageLocalizableTitle);
            miniTables.getController().reAssignTable(list, null);
            miniTables.show();

        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    @FXML private void currencies(ActionEvent event) {
        Stage currenciesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Currencies.class.getSimpleName());
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
        Stage currencyRatesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, CurrencyRates.class.getSimpleName());
        if(currencyRatesStage == null || !currencyRatesStage.isShowing()){
            CurrencyRates currencyRates = new CurrencyRates(AmbroAFB.mainStage);
            currencyRates.show();
            
            CurrencyRateFilter filter = new CurrencyRateFilter(currencyRates);
            FilterModel model = filter.getResult();
            currencyRates.getCurrencyRatesController().reAssignTable(model);

            if (model.isCanceled()){
                currencyRates.close();
            }
        }
        else {
            currencyRatesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currencyRatesStage);
        }
    }
    
    @FXML private void discountsOnCount(ActionEvent event) {
        Stage discountOnCountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, DiscountOnCounts.class.getSimpleName());
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
        Stage balAccountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, BalanceAccounts.class.getSimpleName());
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
    
    
    @FXML private void goToMonitoring(ActionEvent event){
        String email = GeneralConfig.getInstance().getUserName();
        String password = GeneralConfig.getInstance().getPassword();
        MonitoringClient monitoring = new MonitoringClient(email, password, authclient.Utils.getDefaultConfig(Names.MONITORING_URL_ON_SERVER, Names.APP_NAME));
        try {
            monitoring.loginAndOpenBrowser(email, password);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthServerException ex) {
//            String msg = GeneralConfig.getInstance().getTitleFor("failed_monitoring_login");
//            new AlertMessage(Alert.AlertType.INFORMATION, ex, msg, "").show();
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
    }        
}
