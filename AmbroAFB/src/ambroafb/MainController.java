/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.balance_accounts.BalanceAccounts;
import ambroafb.clients.Client;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.configuration.Configuration;
import ambroafb.countries.Country;
import ambroafb.currencies.Currency;
import ambroafb.currency_rates.CurrencyRate;
import ambroafb.currency_rates.filter.CurrencyRateFilter;
import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.docs.Doc;
import ambroafb.docs.filter.DocFilter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general_scene.doc_table_list.DocTableList;
import ambroafb.general_scene.table_list.TableList;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.filter.InvoiceFilter;
import ambroafb.licenses.License;
import ambroafb.licenses.filter.LicenseFilter;
import ambroafb.loggings.Logging;
import ambroafb.loggings.filter.LoggingFilter;
import ambroafb.minitables.attitudes.Attitude;
import ambroafb.minitables.merchandises.Merchandise;
import ambroafb.params_general.ParamGeneral;
import ambroafb.products.Product;
import authclient.AuthServerException;
import authclient.monitoring.MonitoringClient;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    
    private GeneralConfig config;
    
    @FXML
    private BorderPane formPane;
    @FXML
    private HBox menusPane;
    
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
        String stageTitle = "clients";
        Stage clientsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(clientsStage == null || !clientsStage.isShowing()){
            TableList clients = new TableList(AmbroAFB.mainStage, Client.class, stageTitle);
            clients.show();
            
            ClientFilter filter = new ClientFilter(clients);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()){
                clients.close();
            }
            else {
                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                            ArrayList<Client> clientsList = Client.getFilteredFromDB(model);
                                            clientsList.sort((Client c1, Client c2) -> c1.getRecId() - c2.getRecId());
                                            return new ArrayList(clientsList);
                                        };
                
                clients.getController().reAssignTable(fetchData);
            }
        }
        else {
            clientsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, clientsStage);
        }
    }
    
    @FXML
    private void loggings(ActionEvent event){
        String stageTitle = "loggings";
        Stage loggingsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(loggingsStage == null || !loggingsStage.isShowing()){
            TableList loggings = new TableList(AmbroAFB.mainStage, Logging.class, stageTitle);
            loggings.getController().removeElementsFromEditorPanel("#delete", "#edit", "#view", "#add");
            loggings.show();
            
            LoggingFilter filter = new LoggingFilter(loggings);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()) {
                loggings.close();
            }
            else {
                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                        return new ArrayList(Logging.getFilteredFromDB(model));
                                                    };
                loggings.getController().reAssignTable(fetchData);
            }
        }
        else {
            loggingsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, loggingsStage);
        }
    }
    
    @FXML 
    private void invoices(ActionEvent event) {
        String stageTitle = "invoices";
        Stage invoicesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(invoicesStage == null || !invoicesStage.isShowing()){
            TableList invoices = new TableList(AmbroAFB.mainStage, Invoice.class, stageTitle);
            invoices.show();
            
            InvoiceFilter filter = new InvoiceFilter(invoices);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()){
                invoices.close();
            }
            else {
                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                        return new ArrayList(Invoice.getFilteredFromDB(model));
                                                    };
                invoices.getController().reAssignTable(fetchData);
            }
        }
        else {
            invoicesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, invoicesStage);
        }
    }
    
    @FXML 
    private void products(ActionEvent event) {
        String stageTitle = "products";
        Stage productsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if (productsStage == null || !productsStage.isShowing()){
            TableList products = new TableList(AmbroAFB.mainStage, Product.class, stageTitle);
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(Product.getAllFromDB());
                                                };
            products.getController().reAssignTable(fetchData);
            products.show();
        }
        else{
            productsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, productsStage);
        }
    }
    
    @FXML 
    private void countries(ActionEvent event) {
        String stageTitle = "countries";
        Stage countriesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if (countriesStage == null || !countriesStage.isShowing()){
            TableList countries = new TableList(AmbroAFB.mainStage, Country.class, stageTitle);
            countries.getController().removeElementsFromEditorPanel("#delete", "#edit", "#view", "#add");

//            Supplier< List<EditorPanelable> > supplier = () -> {
//                ArrayList<EditorPanelable> list = new ArrayList<>();
//                list.addAll(Country.getAllFromDB());
//                return list;
//            };
//            FetchTableListEntriesFromDB fetch = new FetchTableListEntriesFromDB(supplier, countries);
//            fetch.start();
            
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(Country.getAllFromDB());
                                                };
            countries.getController().reAssignTable(fetchData);
            countries.show();
        }
        else {
            countriesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, countriesStage);
        }
        
    }
    
    
    
    @FXML private void accounts(ActionEvent event) { System.out.println("cleack works!");}
    
    @FXML private void docs(ActionEvent event){
        String stageTitle = "docs";
        Stage docsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(docsStage == null || !docsStage.isShowing()){
            DocTableList docs = new DocTableList(AmbroAFB.mainStage, Doc.class, stageTitle);
            docs.show();
            
            Filterable filter = new DocFilter(docs);
            FilterModel model = filter.getResult();
            if (model.isCanceled()){
                docs.close();
            }
            else {
                Supplier<ArrayList<Doc>> fetchData = () -> {
                                                            return new ArrayList(Doc.getFilteredFromDB(model));
                                                        };
                docs.getController().reAssignTable(fetchData);
            }
        }
        else {
            docsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, docsStage);
        }
    }
    
    @FXML private void licenses(ActionEvent event) {
        String stageTitle = "licenses";
        Stage licensesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(licensesStage == null || !licensesStage.isShowing()){
            TableList licenses = new TableList(AmbroAFB.mainStage, License.class, stageTitle);
            licenses.getController().removeElementsFromEditorPanel("#delete", "#edit", "#view", "#add");
            licenses.show();
            
            LicenseFilter filter = new LicenseFilter(licenses);
            FilterModel filterModel = filter.getResult();

            if (filterModel.isCanceled()){
                licenses.close();
            }
            else {
                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                        return new ArrayList(License.getFilteredFromDB(filterModel));
                                                    };
                licenses.getController().reAssignTable(fetchData);
            }
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
    
    
    @FXML
    private void attitudes(ActionEvent event){
        String stageTitle = "attitudes";
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            TableList attitudes = new TableList(AmbroAFB.mainStage, Attitude.class, stageTitle);
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(Attitude.getAllFromDB());
                                                };
            attitudes.getController().reAssignTable(fetchData);
            attitudes.getController().removeElementsFromEditorPanel("#search");
            attitudes.show();
            
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    
    @FXML
    private void merchandises(ActionEvent event){
        String stageTitle = "merchandises";
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            TableList merchandises = new TableList(AmbroAFB.mainStage, Merchandise.class, stageTitle);
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(Merchandise.getAllFromDB());
                                                };
            merchandises.getController().reAssignTable(fetchData);
            merchandises.getController().removeElementsFromEditorPanel("#search");
            merchandises.show();
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    @FXML private void generalParams(ActionEvent event){
        Stage paramsGeneralStage = StagesContainer.getStageFor(AmbroAFB.mainStage, ParamGeneral.class.getSimpleName());
        if(paramsGeneralStage == null || !paramsGeneralStage.isShowing()){
            TableList generalParams = new TableList(AmbroAFB.mainStage, ParamGeneral.class, "general_params");
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(ParamGeneral.getAllFromDB());
                                                };
            generalParams.getController().reAssignTable(fetchData);
            generalParams.getController().removeElementsFromEditorPanel("#refresh");
            generalParams.show();
        }
        else {
            paramsGeneralStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, paramsGeneralStage);
        }
    }
        
    @FXML private void currencies(ActionEvent event) {
        String stageTitle = "currencies";
        Stage currenciesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(currenciesStage == null || !currenciesStage.isShowing()){
            TableList currencies = new TableList(AmbroAFB.mainStage, Currency.class, stageTitle);
            currencies.getController().removeElementsFromEditorPanel("#search");
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(Currency.getAllFromDB());
                                                };
            currencies.getController().reAssignTable(fetchData);
            currencies.show();
        }
        else {
            currenciesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currenciesStage);
        }
    }
    
    @FXML private void currencyRates(ActionEvent event) {
        String stageTitle = "currency_rates";
        Stage currencyRatesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if(currencyRatesStage == null || !currencyRatesStage.isShowing()){
            TableList currencyRates = new TableList(AmbroAFB.mainStage, CurrencyRate.class, stageTitle);
            currencyRates.show();
            
            CurrencyRateFilter filter = new CurrencyRateFilter(currencyRates);
            FilterModel model = filter.getResult();

            if (model.isCanceled()){
                currencyRates.close();
            }
            else {
                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                        return new ArrayList(CurrencyRate.getFilteredFromDB(model));
                                                    };
                currencyRates.getController().reAssignTable(fetchData);
            }
        }
        else {
            currencyRatesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currencyRatesStage);
        }
    }
    
    @FXML private void discountsOnCount(ActionEvent event) {
        String stageTitle = "discounts";
        Stage discountOnCountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if (discountOnCountsStage == null || !discountOnCountsStage.isShowing()){
            TableList discountOnCounts = new TableList(AmbroAFB.mainStage, DiscountOnCount.class, stageTitle);
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> {
                                                    return new ArrayList(DiscountOnCount.getAllFromDB());
                                                };
            discountOnCounts.getController().reAssignTable(fetchData);
            discountOnCounts.getController().removeElementsFromEditorPanel("#search");
            discountOnCounts.show();
        }
        else {
            discountOnCountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, discountOnCountsStage);
        }
    }
    
    @FXML private void balAccounts(ActionEvent event) {
        String stageTitle = "balanceaccounts";
        Stage balAccountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, stageTitle);
        if (balAccountsStage == null || !balAccountsStage.isShowing()){
            BalanceAccounts balAccounts = new BalanceAccounts(AmbroAFB.mainStage);
            Supplier<ArrayList<EditorPanelable>> fetchData = () -> new ArrayList(BalanceAccount.getAllFromDB());
            balAccounts.getBalanceAccountsController().reAssignTable(fetchData);
            balAccounts.show();
        }
        else {
            balAccountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, balAccountsStage);
        }
    }
    
    @FXML private void balance(ActionEvent event) {}
    @FXML private void income_statement(ActionEvent event) {}
    @FXML private void other(ActionEvent event) {}
    
    
    @FXML private void goToMonitoring(ActionEvent event){
        String email = GeneralConfig.getInstance().getUserName();
        String password = GeneralConfig.getInstance().getPassword();
        MonitoringClient monitoring = new MonitoringClient(email, password, authclient.Utils.getDefaultConfig(Names.MONITORING_URL_ON_SERVER, Names.APP_NAME));
        try {
            monitoring.loginAndOpenBrowser(email, password);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
//            String msg = GeneralConfig.getInstance().getTitleFor("failed_monitoring_login");
//            new AlertMessage(Alert.AlertType.INFORMATION, ex, msg, "").show();

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
    
    
    private class FetchTableListEntriesFromDB extends Thread {

        private final Supplier< List<EditorPanelable> > supplier;
        private final TableList tableList;
        
        public FetchTableListEntriesFromDB(Supplier< List<EditorPanelable> > supplier, TableList tbList){
            this.supplier = supplier;
            tableList = tbList;
        }
        
        @Override
        public void run() {
            List<EditorPanelable> contentList = supplier.get();
            Platform.runLater(() -> {
//                Supplier<ArrayList<EditorPanelable>> fetchData = () -> {};
//                tableList.getController().reAssignTable(contentList, null);
                tableList.show();
            });
        }
        
    }
}
