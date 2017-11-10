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
import ambroafb.accounts.detail_pane.helper.AccountEntry;
import ambroafb.accounts.detail_pane.helper.AccountReview;
import ambroafb.general.GeneralConfig;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.general_scene.table_master_detail.MasterObserver;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.MaskerPane;
import org.json.JSONArray;
import org.json.JSONException;

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
    private MaskerPane masker;
    
    @FXML
    private Region headerEmptyRegion, footerEmptyRegion;
    
    
    private AccountDataFetchProvider dataFetchProvider;
    private Account selectedAccount;
    private final double maskerOpacity = 0.4;
    
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
        
        // -----
        headerEmptyRegion.setMinWidth(Double.parseDouble(TableColumnWidths.SLIDER));
        headerEmptyRegion.setMaxWidth(Double.parseDouble(TableColumnWidths.SLIDER));
        footerEmptyRegion.setMinWidth(Double.parseDouble(TableColumnWidths.SLIDER));
        footerEmptyRegion.setMaxWidth(Double.parseDouble(TableColumnWidths.SLIDER));
    }
    
    private void setComponentsFeatures(){
        accountEntries.setBundle(GeneralConfig.getInstance().getBundle());
        compute.setOnAction(this::compute);
        masker.setOpacity(maskerOpacity);
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
    
    private void fetchEntries(){
        Consumer<JSONArray> successAction = (data) -> {
            try {
                JSONArray reviewData = data.getJSONArray(0);
                ArrayList<AccountReview> reviewList = Utils.getListFromJSONArray(AccountReview.class, reviewData);
                JSONArray entries = data.getJSONArray(1);
                ArrayList<AccountEntry> entryList = Utils.getListFromJSONArray(AccountEntry.class, entries);
            
                AccountReview header = reviewList.stream().filter(review -> review.isTo()).findFirst().get();
                AccountReview footer = reviewList.stream().filter(review -> !review.isTo()).findFirst().get();
                updateAmountInfo(header, currentDebitInfo, currentCreditInfo);
                updateAmountInfo(footer, startingDebitInfo, startingCreditInfo);
                accountEntries.setItems(FXCollections.observableArrayList(entryList));
                
                masker.setVisible(false);
            } catch (JSONException ex) {
                Logger.getLogger(AccountDetailPane.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        };
        masker.setVisible(true);
        dataFetchProvider.getAccountEntries(selectedAccount.getRecId(), beginDate.getValue(), endDate.getValue(), successAction, null);
    }
    
    private void updateAmountInfo(AccountReview data, Label debit, Label credit){
        String debitText = "";
        Color debitColor = Color.BLACK;
        String creditText = "";
        Color creditColor = Color.BLACK;
        if (data.isDebit()){
            debitText = NumberConverter.makeFloatStringBySpecificFraction(data.getAmount(), 2);
            if (data.isRed()) debitColor = Color.RED;
        }
        else {
            creditText = NumberConverter.makeFloatStringBySpecificFraction(data.getAmount(), 2);
            if (data.isRed()) creditColor = Color.RED;
        }
        debit.setText(debitText);
        debit.setTextFill(debitColor);
        
        credit.setText(creditText);
        credit.setTextFill(creditColor);
    }

    @Override
    public void notify(EditorPanelable selected) {
        selectedAccount = (Account) selected;
        accountNumberIso.setText(selectedAccount.getAccount() + " / " + selectedAccount.getIso());
        accountDescrip.setText(selectedAccount.getDescrip());

        balAccNumber.setText("" + selectedAccount.getBalAccount());
        balAccDescrip.setText(selectedAccount.getBalAccDescrip());

        String clientIdText = (selectedAccount.getClientId() == 0) ? "" : "" + selectedAccount.getClientId();
        clientId.setText(clientIdText);
        clientDescrip.setText(selectedAccount.getClientDescrip());
    }

    @Override
    public void update(EditorPanelable selected) {
        fetchEntries();
    }
    
}
