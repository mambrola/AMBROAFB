/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.accounts.Account;
import ambroafb.accounts.AccountDataFetchProvider;
import ambroafb.accounts.AccountManager;
import ambroafb.accounts.detail_pane.AccountDetailPane;
import ambroafb.accounts.filter.AccountFilter;
import ambroafb.balance_accounts.BalAccountManager;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.balance_accounts.BalanceAccounts;
import ambroafb.balances.Balance;
import ambroafb.balances.BalanceManager;
import ambroafb.clients.Client;
import ambroafb.clients.ClientManager;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.configuration.Configuration;
import ambroafb.countries.Country;
import ambroafb.countries.CountryManager;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyManager;
import ambroafb.currency_rates.CurrencyRate;
import ambroafb.currency_rates.CurrencyRateManager;
import ambroafb.currency_rates.filter.CurrencyRateFilter;
import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.discounts_on_count.DiscountOnCountManager;
import ambroafb.docs.Doc;
import ambroafb.docs.filter.DocFilter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.doc.DocEditorPanel;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general_scene.table_list.TableList;
import ambroafb.general_scene.table_master_detail.TableMasterDetail;
import ambroafb.general_scene.table_master_detail.TableMasterDetailController;
import ambroafb.general_scene.tree_table_list.TreeTableList;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.InvoiceManager;
import ambroafb.invoices.filter.InvoiceFilter;
import ambroafb.licenses.License;
import ambroafb.licenses.LicenseManager;
import ambroafb.licenses.filter.LicenseFilter;
import ambroafb.loggings.Logging;
import ambroafb.loggings.LoggingManager;
import ambroafb.loggings.filter.LoggingFilter;
import ambroafb.minitables.attitudes.Attitude;
import ambroafb.minitables.attitudes.AttitudeManager;
import ambroafb.minitables.merchandises.Merchandise;
import ambroafb.minitables.merchandises.MerchandiseManager;
import ambroafb.params_general.ParamGeneral;
import ambroafb.params_general.ParamGeneralManager;
import ambroafb.products.Product;
import ambroafb.products.ProductManager;
import authclient.monitoring.MonitoringClient;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = GeneralConfig.getInstance();
    }
    
    @FXML
    private void mainConfig(ActionEvent event) {
        Stage configStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Names.CONFIGURATION);
        
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
    
    @FXML private void accounts(ActionEvent event) { 
        String stageTitle = "accounts";
        Stage accountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Account.class.getSimpleName());
        if(accountsStage == null || !accountsStage.isShowing()){
            TableMasterDetail accounts = new TableMasterDetail(AmbroAFB.mainStage, Account.class, stageTitle);
            AccountManager manager = new AccountManager();
            accounts.setEPManager(manager);
            AccountDetailPane detailPane = new AccountDetailPane();
            detailPane.setDataFetchProvider((AccountDataFetchProvider)manager.getDataFetchProvider());
            ((TableMasterDetailController)accounts.getController()).setDetailNode(detailPane);
            ((TableMasterDetailController)accounts.getController()).registerObserver(detailPane);
            accounts.show();
            
            AccountFilter filter = new AccountFilter(accounts);
            FilterModel filterModel = filter.getResult();
            if (filterModel.isCanceled()){
                accounts.close();
            } else {
                accounts.getController().reAssignTable(filterModel);
            }
        }
        else {
            accountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, accountsStage);
        }
    }
    
    @FXML
    private void clients(ActionEvent event) {
        String stageTitle = "clients";
        Stage clientsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Client.class.getSimpleName());
        if(clientsStage == null || !clientsStage.isShowing()){
            TableList clients = new TableList(AmbroAFB.mainStage, Client.class, stageTitle);
            clients.setEPManager(new ClientManager());
            clients.getController().getEditorPanel().hideMenuOnAddButton();
            clients.show();
            
            ClientFilter filter = new ClientFilter(clients);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()){
                clients.close();
            }
            else {
                clients.getController().reAssignTable(model);
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
        Stage loggingsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Logging.class.getSimpleName());
        if(loggingsStage == null || !loggingsStage.isShowing()){
            TableList loggings = new TableList(AmbroAFB.mainStage, Logging.class, stageTitle);
            loggings.setEPManager(new LoggingManager());
            loggings.getController().getEditorPanel().removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID);
            loggings.show();
            
            LoggingFilter filter = new LoggingFilter(loggings);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()) {
                loggings.close();
            }
            else {
                loggings.getController().reAssignTable(model);
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
        Stage invoicesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Invoice.class.getSimpleName());
        if(invoicesStage == null || !invoicesStage.isShowing()){
            TableList invoices = new TableList(AmbroAFB.mainStage, Invoice.class, stageTitle);
            invoices.setEPManager(new InvoiceManager());
            invoices.show();
            
            InvoiceFilter filter = new InvoiceFilter(invoices);
            FilterModel model = filter.getResult();
            
            if (model.isCanceled()){
                invoices.close();
            }
            else {
                invoices.getController().reAssignTable(model);
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
        Stage productsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Product.class.getSimpleName());
        if (productsStage == null || !productsStage.isShowing()){
            TableList products = new TableList(AmbroAFB.mainStage, Product.class, stageTitle);
            products.setEPManager(new ProductManager());
            products.getController().reAssignTable(null);
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
        Stage countriesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Country.class.getSimpleName());
        if (countriesStage == null || !countriesStage.isShowing()){
            TableList countries = new TableList(AmbroAFB.mainStage, Country.class, stageTitle);
            countries.setEPManager(new CountryManager());
            countries.getController().getEditorPanel().removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID);

            countries.getController().reAssignTable(null);
            countries.show();
        }
        else {
            countriesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, countriesStage);
        }
        
    }
    
    
    @FXML private void docs(ActionEvent event){
        String stageTitle = "docs";
        Stage docsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Doc.class.getSimpleName());
        if(docsStage == null || !docsStage.isShowing()){
            DocEditorPanel docEditorPanel = new DocEditorPanel();
            TableList docs = new TableList(AmbroAFB.mainStage, Doc.class, stageTitle, docEditorPanel);
            docs.setEPManager(new ambroafb.docs.DocManager());
            docs.show();
            
            Filterable filter = new DocFilter(docs);
            FilterModel model = filter.getResult();
            if (model.isCanceled()){
                docs.close();
            }
            else {
                docs.getController().reAssignTable(model);
            }
        }
        else {
            docsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, docsStage);
        }
    }
    
    @FXML private void licenses(ActionEvent event) {
        String stageTitle = "licenses";
        Stage licensesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, License.class.getSimpleName());
        if(licensesStage == null || !licensesStage.isShowing()){
            TableList licenses = new TableList(AmbroAFB.mainStage, License.class, stageTitle);
            licenses.setEPManager(new LicenseManager());
            licenses.getController().getEditorPanel().removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID);
            licenses.show();
            
            LicenseFilter filter = new LicenseFilter(licenses);
            FilterModel filterModel = filter.getResult();

            if (filterModel.isCanceled()){
                licenses.close();
            }
            else {
                licenses.getController().reAssignTable(filterModel);
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
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Attitude.class.getSimpleName());
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            TableList attitudes = new TableList(AmbroAFB.mainStage, Attitude.class, stageTitle);
            attitudes.setEPManager(new AttitudeManager());
            attitudes.getController().getEditorPanel().removeComponents(EditorPanel.SEARCH_FXID);
            attitudes.getController().reAssignTable(null);
            attitudes.show();
            
        } else {
            miniTablesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, miniTablesStage);
        }
    }
    
    
    @FXML
    private void merchandises(ActionEvent event){
        String stageTitle = "merchandises";
        Stage miniTablesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Merchandise.class.getSimpleName());
        if(miniTablesStage == null || !miniTablesStage.isShowing()){
            TableList merchandises = new TableList(AmbroAFB.mainStage, Merchandise.class, stageTitle);
            merchandises.setEPManager(new MerchandiseManager());
            merchandises.getController().getEditorPanel().removeComponents(EditorPanel.SEARCH_FXID);
            merchandises.getController().reAssignTable(null);
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
            generalParams.setEPManager(new ParamGeneralManager());
            generalParams.getController().reAssignTable(null);
            generalParams.getController().getEditorPanel().removeComponents(EditorPanel.REFRESH_FXID);
            generalParams.show();
        }
        else {
            paramsGeneralStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, paramsGeneralStage);
        }
    }
        
    @FXML private void currencies(ActionEvent event) {
        String stageTitle = "currencies";
        Stage currenciesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Currency.class.getSimpleName());
        if(currenciesStage == null || !currenciesStage.isShowing()){
            TableList currencies = new TableList(AmbroAFB.mainStage, Currency.class, stageTitle);
            currencies.setEPManager(new CurrencyManager());
            currencies.getController().getEditorPanel().removeComponents(EditorPanel.SEARCH_FXID);
            currencies.getController().getEditorPanel().hideMenuOnAddButton();
            currencies.getController().reAssignTable(null);
            currencies.show();
        }
        else {
            currenciesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currenciesStage);
        }
    }
    
    @FXML private void currencyRates(ActionEvent event) {
        String stageTitle = "currency_rates";
        Stage currencyRatesStage = StagesContainer.getStageFor(AmbroAFB.mainStage, CurrencyRate.class.getSimpleName());
        if(currencyRatesStage == null || !currencyRatesStage.isShowing()){
            TableList currencyRates = new TableList(AmbroAFB.mainStage, CurrencyRate.class, stageTitle);
            currencyRates.setEPManager(new CurrencyRateManager());
            currencyRates.show();
            
            CurrencyRateFilter filter = new CurrencyRateFilter(currencyRates);
            FilterModel model = filter.getResult();

            if (model.isCanceled()){
                currencyRates.close();
            }
            else {
                currencyRates.getController().reAssignTable(model);
            }
        }
        else {
            currencyRatesStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, currencyRatesStage);
        }
    }
    
    @FXML private void discountsOnCount(ActionEvent event) {
        String stageTitle = "discounts";
        Stage discountOnCountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, DiscountOnCount.class.getSimpleName());
        if (discountOnCountsStage == null || !discountOnCountsStage.isShowing()){
            TableList discountOnCounts = new TableList(AmbroAFB.mainStage, DiscountOnCount.class, stageTitle);
            discountOnCounts.setEPManager(new DiscountOnCountManager());
            discountOnCounts.getController().getEditorPanel().removeComponents(EditorPanel.SEARCH_FXID);
            discountOnCounts.getController().reAssignTable(null);
            discountOnCounts.show();
        }
        else {
            discountOnCountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, discountOnCountsStage);
        }
    }
    
    @FXML private void balAccounts(ActionEvent event) {
        String stageTitle = "bal_accounts";
        Stage balAccountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, BalanceAccount.class.getSimpleName());
        if (balAccountsStage == null || !balAccountsStage.isShowing()){
            BalanceAccounts balAccounts = new BalanceAccounts(AmbroAFB.mainStage, BalanceAccount.class, stageTitle);
            balAccounts.setEPManager(new BalAccountManager());
            balAccounts.getController().reAssignTable(null);
            balAccounts.show();
        }
        else {
            balAccountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, balAccountsStage);
        }
    }
    
    @FXML private void balance(ActionEvent event) {
        String stageTitle = "balance";
        Stage balAccountsStage = StagesContainer.getStageFor(AmbroAFB.mainStage, Balance.class.getSimpleName());
        if (balAccountsStage == null || !balAccountsStage.isShowing()){
            TreeTableList balances = new TreeTableList(AmbroAFB.mainStage, Balance.class, stageTitle);
            // Change to SpectatorPanel ---
            balances.getController().getEditorPanel().removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID);
            
            Function<List<EditorPanel>, ObservableList<EditorPanel>> makeTree = (balanseList) -> {
                ObservableList<EditorPanel> roots = FXCollections.observableArrayList();
                
                return roots;
            };
            balances.setEPManager(new BalanceManager());
            balances.getController().reAssignTable(null);
            balances.show();
        }
        else {
            balAccountsStage.requestFocus();
            StageUtils.centerChildOf(AmbroAFB.mainStage, balAccountsStage);
        }
    }
    
    @FXML private void income_statement(ActionEvent event) {}
    @FXML private void other(ActionEvent event) {}
    
    
    @FXML private void goToMonitoring(ActionEvent event){
        String email = config.getUserName();
        String password = config.getPassword();
        MonitoringClient monitoring = new MonitoringClient(email, password, authclient.Utils.getDefaultConfig(Names.MONITORING_URL_ON_SERVER, Names.APP_NAME));
        try {
            monitoring.loginAndOpenBrowser(email, password);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
