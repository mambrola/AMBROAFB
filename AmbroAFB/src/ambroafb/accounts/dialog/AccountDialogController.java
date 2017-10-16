/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambro.ADatePicker;
import ambroafb.accounts.Account;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.balance_accounts.BalanceAccountTreeComboBox;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.account_number.AccountNumber;
import ambroafb.general.scene_components.account_number.NumberGenerateManager;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class AccountDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker openDate;
    @FXML
    private IsoComboBox currencies;
    @FXML
    private BalanceAccountTreeComboBox balAccounts;
    @FXML
    private AccountNumber accountNumber;
    @FXML
    private ClientComboBox clients;
    @FXML
    private TextField descrip, remark;
    @FXML
    private ADatePicker closeDate;
    
    
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        accountNumber.setNumberGenerateManager(new CustomNumberGenerator());
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Account account = (Account)object;
            openDate.valueProperty().bindBidirectional(account.openedProperty());
            currencies.valueProperty().bindBidirectional(account.isoProperty());
            balAccounts.valueProperty().bindBidirectional(account.balAccProperty());
            clients.valueProperty().bindBidirectional(account.clientProperty());
            accountNumber.valueProperty().bindBidirectional(account.accountNumberProperty());
            descrip.textProperty().bindBidirectional(account.descripProperty());
            closeDate.valueProperty().bindBidirectional(account.closedProperty());
            remark.textProperty().bindBidirectional(account.remarkProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType) {
        Consumer<ObservableList<BalanceAccount>> setBalAccByNumber = (balAccList) -> {
            int balAccountNumber = ((Account)object).getbalAccount();
            Optional<BalanceAccount> optBalAcc = balAccList.stream().filter((balAcc) -> balAcc.getBalAcc() == balAccountNumber).findFirst();
            if (optBalAcc.isPresent()){
                balAccounts.setValue(null); // რადგან balAccount_ებისთვის მხოლოდ Number მოდის ბაზიდან buttonCell-ში ჩნდება მარტო Number. ამიტომ ვაკეთებთ ამ ერთგვარ "refresh"-ს.
                balAccounts.setValue(optBalAcc.get());
            }
        };
        balAccounts.fillComboBoxWithoutALL(setBalAccByNumber);
        
        Consumer<ObservableList<Client>> setClientbyId = (clientsList) -> {
            int clientId = ((Account)object).getClientId();
            Optional<Client> optClient = clientsList.stream().filter((client) -> client.getRecId() == clientId).findFirst();
            if (optClient.isPresent()){
                clients.valueProperty().set(null);
                clients.valueProperty().set(optClient.get());
            }
        };
        clients.fillComboBoxWithClientsAndPartners(setClientbyId);
    }
    
    
    private class CustomNumberGenerator implements NumberGenerateManager {

        private final String procedureNameForKey = "account_set_key";
        private final String procedureNameForNew = "account_get_fit_account";
        private final String responseKey = "account";
        private final DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        private final String emptyServerResponse = "No account number";
        
        @Override
        public void generateKeyFor(Consumer<String> success, Consumer<Exception> error) {
            if (!accountNumber.getText().isEmpty()){
                try {
                    String accNum = getAccountNumber(procedureNameForKey, Integer.parseInt(accountNumber.getText()));
                    success.accept(accNum);
                } catch (IOException | AuthServerException | JSONException ex) {
                    error.accept(ex);
                }
            }
        }

        @Override
        public void generateNewNumber(Consumer<String> success, Consumer<Exception> error) {
            if (clients.getSelectionModel().getSelectedIndex() >= 0 && balAccounts.valueProperty().isNotNull().get() && currencies.valueProperty().isNotNull().get()){
                try {
                    String accNum = getAccountNumber(procedureNameForNew, clients.valueProperty().get().getRecId(), balAccounts.getValue().getBalAcc(), currencies.getValue());
                    success.accept(accNum);
                } catch (IOException | AuthServerException | JSONException ex) {
                    error.accept(ex);
                }
            }
            else {
                success.accept("");
            }
        }
        
        private String getAccountNumber(String procedureName, Object... params) throws IOException, AuthServerException, JSONException{
            String resposne = dbClient.callProcedure(procedureName, params).getDataAsString();
            JSONArray accountsNumber = new JSONArray(resposne);
            return (!accountsNumber.isNull(0)) 
                                    ? accountsNumber.getJSONObject(0).optString(responseKey) 
                                    : emptyServerResponse;
        }
    }
}
