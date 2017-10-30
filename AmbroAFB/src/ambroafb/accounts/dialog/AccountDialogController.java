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
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.account_number.AccountNumber;
import ambroafb.general.scene_components.account_number.NumberGenerateManager;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class AccountDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
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
    protected void makeExtraActions(Names.EDITOR_BUTTON_TYPE buttonType) {
        Consumer<ObservableList<BalanceAccount>> setBalAccByNumber = (balAccList) -> {
            int balAccountNumber = ((Account)sceneObj).getBalAccount();
            Optional<BalanceAccount> optBalAcc = balAccList.stream().filter((balAcc) -> balAcc.getBalAcc() == balAccountNumber).findFirst();
            if (optBalAcc.isPresent()){
                balAccounts.setValue(null); // რადგან balAccount_ებისთვის მხოლოდ Number მოდის ბაზიდან buttonCell-ში ჩნდება მარტო Number. ამიტომ ვაკეთებთ ამ ერთგვარ "refresh"-ს.
                balAccounts.setValue(optBalAcc.get());
            }
        };
        balAccounts.fillComboBoxWithoutALL(setBalAccByNumber);
        
        Consumer<ObservableList<Client>> setClientbyId = (clientsList) -> {
            int clientId = ((Account)sceneObj).getClientId();
            Optional<Client> optClient = clientsList.stream().filter((client) -> client.getRecId() == clientId).findFirst();
            if (optClient.isPresent()){
                clients.valueProperty().set(null);
                clients.valueProperty().set(optClient.get());
            }
        };
        clients.fillComboBoxWithClientsAndPartners(setClientbyId);
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD_SAMPLE)){
            ((Account)sceneObj).setDateOpen(LocalDate.now().toString());
            ((Account)sceneObj).setDateClose("");
            ((Account)sceneObj).accountNumberProperty().set("");
            ((Account)sceneObj).setRemark("");
            backupObj.copyFrom(sceneObj);
        }
    }
    
    
    private class CustomNumberGenerator implements NumberGenerateManager {

        private final String procedureNameForKey = "account_set_key";
        private final String procedureNameForNew = "account_insert_update_get_fit_account";
        
        private final String accountNumberJsonKey = "account";
        private final String accountExistsJsonKey = "accountExists";
        private final String accountInOtherISOJsonKey = "accountInOtherIso";
        private final String accountNewJsonKey = "accountNew";
        private final String fitFlagJsonKey = "fitFlag";
        private final String descripJsonKey = "descrip";
        
        private final String descripBundleKey = "descrip";
        private final String accountNumberBundleKey = "account";
        
        private final DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        private final String emptyServerResponse = "No account number";
        private int fitFlag = 0;
        
        @Override
        public void generateKeyFor(Consumer<String> success, Consumer<Exception> error) {
            new Thread(() -> {
                try {
                    int accountNum = (accountNumber.getText().isEmpty()) ? 0 : Integer.parseInt(accountNumber.getText());
                    String accNum = getAccountNumber(accountNum);
                    Platform.runLater(() -> {
                        if (success != null) success.accept(accNum);
                    });
                } catch (IOException | AuthServerException | JSONException ex) {
                    Platform.runLater(() -> {
                        if (error != null) error.accept(ex);
                    });
                }
            }).start();
        }
        
        private String getAccountNumber(int accNumWithoutKey) throws IOException, AuthServerException, JSONException {
            JSONArray data = dbClient.callProcedureAndGetAsJson(procedureNameForKey, accNumWithoutKey);
            System.out.println("getAccountNumber data: " + data);
            return (!data.isNull(0)) ? "" + data.getJSONObject(0).optInt(accountNumberJsonKey) : emptyServerResponse;
        }

        @Override
        public void generateNewNumber(Consumer<String> success, Consumer<Exception> error) {
            if (balAccounts.valueProperty().isNotNull().get() && currencies.valueProperty().isNotNull().get()){
                int accountNum = (accountNumber.getText().isEmpty()) ? 0 : Integer.parseInt(accountNumber.getText());
                Function<JSONObject, ButtonType> warningFN = (JSONObject obj) -> {
                    String headerText = getWarningMessageFrom(obj);
                    String numberSp = String.format("|%20s|", GeneralConfig.getInstance().getTitleFor(accountNumberBundleKey));
                    String descripSp = String.format("|%20s|", GeneralConfig.getInstance().getTitleFor(descripBundleKey));
                    String contentText = numberSp + ":\t" + obj.optInt(accountNumberJsonKey) + "\n" +
                                        descripSp + ":\t" + obj.optString(descripJsonKey);
                    AlertMessage alert = new AlertMessage((Stage)formPane.getScene().getWindow(), Alert.AlertType.CONFIRMATION, headerText, contentText);
                    return alert.showAndWait().get();
                };
                Integer clientID = (clients.getSelectionModel().getSelectedIndex() >= 0) ? clients.valueProperty().get().getRecId() : null;
                askNewAccountNumberToDB(success, warningFN, error, accountNum,
                                    clientID, balAccounts.getValue().getBalAcc(), currencies.getValue(), fitFlag);
            }
            else {
                success.accept("");
            }
        }
        
        private void askNewAccountNumberToDB(Consumer<String> success, Function<JSONObject, ButtonType> warning, Consumer<Exception> error,
                                        int accNum, Integer clientId, int balAcc, String iso, int fitflag) {
            new Thread(() -> {
                try {
                    JSONArray data = dbClient.callProcedureAndGetAsJson(procedureNameForNew, accNum, clientId, balAcc, iso, fitflag);
                    System.out.println("account data: " + data);
                    if (!data.isNull(0)){
                        JSONObject obj = data.getJSONObject(0);
                        if (obj.length() == 1 || obj.has(accountNewJsonKey)){
                            Platform.runLater(() -> {
                                if (success != null) {
                                    fitFlag = obj.optInt(fitFlagJsonKey);
                                    success.accept("" + obj.optInt(accountNumberJsonKey));
                                }
                            });
                        }
                        else {
                            Platform.runLater(() -> {
                                if (warning != null){
                                    if (warning.apply(obj).equals(ButtonType.OK)){
                                        int newAccNum = obj.optInt(accountNumberJsonKey);
                                        int newFitFlag = obj.optInt(fitFlagJsonKey);
                                        askNewAccountNumberToDB(success, warning, error, 
                                                            newAccNum, clientId, balAcc, iso, newFitFlag);
                                    }
                                }
                            });
                        }
                    }
                } catch (IOException | AuthServerException | JSONException  ex) {
                    Platform.runLater(() -> {
                        if (error != null) error.accept(ex);
                    });
                }
                
            }).start();
        }
        
        private String getWarningMessageFrom(JSONObject obj){
            String key = "";
            if (obj.has(accountExistsJsonKey)){
                key = accountExistsJsonKey;
            }
            else if (obj.has(accountInOtherISOJsonKey)){
                key = accountInOtherISOJsonKey;
            }
            else if (obj.has(accountNewJsonKey)){
                key = accountNewJsonKey;
            }
            return GeneralConfig.getInstance().getTitleFor(key);
        }
        
    }
}
