/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane;

import ambro.ADatePicker;
import ambro.ATableView;
import ambroafb.accounts.Account;
import ambroafb.accounts.AccountDataFetchProvider;
import ambroafb.accounts.detail_pane.helper.AccountCommonInfo;
import ambroafb.accounts.detail_pane.helper.AccountEntry;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.general_scene.table_master_detail.MasterObserver;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.MaskerPane;

/**
 *
 * @author dkobuladze
 */
public class AccountDetailPane extends VBox implements MasterObserver  {
    
    @FXML
    private Label   accountNumberIso, accountDescrip,
                    balAccNumber, balAccDescrip,
                    clientId, clientDescrip,
                    currentDebitInfo, currentCreditInfo, startingDebitInfo, startingCreditInfo;
    
    @FXML
    private ADatePicker endDate, beginDate;
    
    @FXML
    private Button compute;
    
    @FXML
    private ATableView<AccountEntry> accountEntries;
    
    @FXML
    private VBox currentDebitPane, currentCreditPane, startingDebitPane, startingCreditPane;
    
    @FXML
    private HBox moneyHeaderPane;
    
    @FXML
    private MaskerPane masker;
    
    
    private AccountDataFetchProvider dataFetchProvider;
    private Account selectedAccount;
    private AccountCommonInfo accountInfo;
    
    public AccountDetailPane(){
        assignLoader();
        setComonentsSize();
        setComponentsFeatures();
        setValues();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AccountDetailPane.class.getResource("/ambroafb/accounts/detail_pane/AccountDetailPane.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(AccountDetailPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setComonentsSize(){
        double dateWidth = Double.parseDouble(TableColumnWidths.DATE);
        endDate.setMinWidth(dateWidth + dateWidth / 2);
        endDate.setMaxWidth(dateWidth + dateWidth / 2);
        
        beginDate.setMinWidth(dateWidth + dateWidth / 2);
        beginDate.setMaxWidth(dateWidth + dateWidth / 2);
        
        double moneyWidth = Double.parseDouble(TableColumnWidths.MONEY);
        currentDebitPane.setMinWidth(moneyWidth);
        currentDebitPane.setMaxWidth(moneyWidth);
        currentCreditPane.setMinWidth(moneyWidth);
        currentCreditPane.setMaxWidth(moneyWidth);
        startingDebitPane.setMinWidth(moneyWidth);
        startingDebitPane.setMaxWidth(moneyWidth);
        startingCreditPane.setMinWidth(moneyWidth);
        startingCreditPane.setMaxWidth(moneyWidth);
    }
    
    private void setComponentsFeatures(){
        accountEntries.setBundle(GeneralConfig.getInstance().getBundle());
        compute.setOnAction(this::compute);
    }
    
    private void compute(ActionEvent event){
        fetchEntries();
    }
    
    private void setValues(){
        LocalDate currDate = LocalDate.now();
        endDate.setValue(currDate);
        beginDate.setValue(currDate.minusDays(7));
    }
    
    public void setDataFetchProvider(AccountDataFetchProvider dataFetcher){
        dataFetchProvider = dataFetcher;
    }
    
    @Deprecated
    public Consumer<EditorPanelable> getUpdateAction(){
        return (selected) -> {
            try {
                selectedAccount = (Account) selected;
                fetchInfo();
                fetchEntries();
            } catch (Exception ex) {
                Logger.getLogger(AccountDetailPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }
    
    private void fetchInfo() throws Exception {
        Consumer<AccountCommonInfo> successAction = (accInfo) -> {
            accountInfo = accInfo;
            accountNumberIso.setText(selectedAccount.getAccount() + " / " + selectedAccount.getIso());
            accountDescrip.setText(accountInfo.getAccountDescrip());

            balAccNumber.setText("" + selectedAccount.getBalAccount());
            balAccDescrip.setText(accountInfo.getBalAccDescrip());

            String clientIdText = (selectedAccount.getClientId() == 0) ? "" : "" + selectedAccount.getClientId();
            clientId.setText(clientIdText);
            clientDescrip.setText(accountInfo.getClientDescrip());
        };
        dataFetchProvider.fetchAccountInfo(selectedAccount.getRecId(), successAction, null);
    }
    
    private void fetchEntries(){
        new Thread(() -> {
            try {
                List<AccountEntry> entries = dataFetchProvider.getAccountEntries(selectedAccount.getRecId(), beginDate.getValue(), endDate.getValue());
                System.out.println("----------- entries size: " + entries.size());
                
                Platform.runLater(() -> {
                    AccountEntry header = entries.remove(0);
                    updateAmountInfo(header, currentDebitInfo, currentCreditInfo);
                    AccountEntry footer = entries.remove(entries.size() - 1);
                    updateAmountInfo(footer, startingDebitInfo, startingCreditInfo);

                    accountEntries.setItems(FXCollections.observableArrayList(entries));
                });
            } catch (Exception ex) {
            }
        }).start();
    }
    
    private void updateAmountInfo(AccountEntry data, Label debit, Label credit){
        String debitText = "";
        Color debitColor = Color.BLACK;
        String creditText = "";
        Color creditColor = Color.BLACK;
        if (data.isDebit()){
            debitText = "" + data.getDebit();
            if (accountInfo != null && accountInfo.isDebitRed()) debitColor = Color.RED;
        }
        else {
            creditText = "" + data.getCredit();
            if (accountInfo != null && accountInfo.isCreditRed()) creditColor = Color.RED;
        }
        debit.setText(debitText);
        debit.setTextFill(debitColor);
        
        credit.setText(creditText);
        credit.setTextFill(creditColor);
    }

    @Override
    public void notify(EditorPanelable selected) {
        Account account = (Account) selected;
        accountNumberIso.setText(account.getAccount() + " / " + account.getIso());
        accountDescrip.setText(account.getDescrip());

        balAccNumber.setText("" + account.getBalAccount());
//        balAccDescrip.setText(account.get); // +++++

        String clientIdText = (account.getClientId() == 0) ? "" : "" + account.getClientId();
        clientId.setText(clientIdText);
        clientDescrip.setText(account.getClientDescrip());
    }

    @Override
    public void update(EditorPanelable selected) {
        
    }
    
}
