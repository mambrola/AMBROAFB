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
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.EditorPanel.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogCloseObserver;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.scene_components.account_number.AccountNumber;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class AccountDialogController extends DialogController implements DialogCloseObserver {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private ADatePicker openDate;
    @FXML
    private IsoComboBox currencies;
    @FXML
    private BalanceAccountTreeComboBox balAccounts;
    @FXML
    private Label accNumTitle;
    @FXML
    private AccountNumber accountNumber;
    @FXML
    private ClientComboBox clients;
    @FXML
    private TextField descrip, remark;
    @FXML
    private ADatePicker closeDate;
    
    CustomNumberGenerator customGenerator = new CustomNumberGenerator();
    
    
    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        balAccounts.valueProperty().addListener((ObservableValue<? extends BalanceAccount> observable, BalanceAccount oldValue, BalanceAccount newValue) -> {
            customGenerator.resetFitFlag();
        });
        currencies.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            customGenerator.resetFitFlag();
        });
        clients.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            customGenerator.resetFitFlag();
        });
        
        accountNumber.getKey().setOnAction(this::accountKeyAction);
        accountNumber.getNext().setOnAction(this::accountNextAction);
        
        currencies.fillComboBox(null);
    }
    
    private void accountKeyAction(ActionEvent event){
        Consumer<String> successAction = (value) -> {
            accountNumber.setText(value);
        };
        customGenerator.generateKeyFor(successAction, null);
    }
    
    private void accountNextAction(ActionEvent event){
        String accNumBundleKey = "account_number";
        String defaultTitle = GeneralConfig.getInstance().getTitleFor(accNumBundleKey);
        Consumer<JSONObject> successAction = (obj) -> {
            accountNumber.setText("" + obj.optInt(customGenerator.accountNumberJsonKey));
            if (obj.length() == 1){
                accNumTitle.setText(defaultTitle);
            }
            else {
                okayCancelController.getOkayButton().setDisable(obj.has(customGenerator.accountExistsJsonKey));
                String message = customGenerator.getWarningMessageFrom(obj);
                accNumTitle.setText(defaultTitle + " - " + message);
                
                descrip.setText(obj.optString(customGenerator.accountDescripJsonKey));
                customGenerator.setFitFlag(obj.optInt(customGenerator.fitFlagJsonKey));
            }
        };
        Consumer<Exception> errorAction = (ex) -> {
            String headerText = "";
            String contentText = ex.getMessage();
            new AlertMessage((Stage)formPane.getScene().getWindow(), Alert.AlertType.ERROR, headerText, contentText).showAndWait();
        };

        accNumTitle.setText(defaultTitle);
        if (balAccounts.valueProperty().isNotNull().get() && currencies.valueProperty().isNotNull().get()){
            Integer accountNum = (!accountNumber.getText().isEmpty()) ? Integer.parseInt(accountNumber.getText()) : null; 
            Integer clientID = (clients.getSelectionModel().getSelectedIndex() >= 0) ? clients.valueProperty().get().getRecId() : null;
            customGenerator.generateNewNumber(successAction, errorAction, accountNum, clientID, balAccounts.getValue().getBalAcc(), currencies.getValue());
        }
        else {
            accountNumber.setText("");
        }
    }
    

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Account account = (Account)object;
            openDate.valueProperty().bindBidirectional(account.openedProperty());
            currencies.valueProperty().bindBidirectional(account.isoProperty());
            accountNumber.valueProperty().bindBidirectional(account.accountNumberProperty());
            descrip.textProperty().bindBidirectional(account.descripProperty());
            closeDate.valueProperty().bindBidirectional(account.closedProperty());
            remark.textProperty().bindBidirectional(account.remarkProperty());
        }
    }

    @Override
    protected void makeExtraActions(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.ADD) || buttonType.equals(EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            openDate.setValue(LocalDate.now());
        }
        
        Account accOnScene = ((Account)sceneObj);
        
        Consumer<ObservableList<BalanceAccount>> setBalAccByNumber = (balAccList) -> {
            Integer balAccountId = accOnScene.getBalAccountId();
            Bindings.bindBidirectional(accOnScene.balAccountIdProperty(), balAccounts.valueProperty(), new BalAccountToIdBiConverter());
            accOnScene.balAccountProperty().bind(Bindings.createStringBinding(() -> (balAccounts.getValue() == null) ? null : "" + balAccounts.getValue().getBalAcc(), balAccounts.valueProperty()));
            accOnScene.setBalAccountId(balAccountId);
        };
        balAccounts.fillComboBoxWithoutALL(setBalAccByNumber);
        
        Consumer<ObservableList<Client>> setClientbyId = (clientsList) -> {
            Integer clientId = accOnScene.getClientId();
            Bindings.bindBidirectional(accOnScene.clientIdProperty(), clients.valueProperty(), new ClientToIdBiConverter());
            accOnScene.setClientId(clientId);
        };
        clients.fillComboBoxWithClientsAndPartners(setClientbyId);
        
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            accOnScene.setDateOpen(LocalDate.now().toString());
            accOnScene.setDateClose("");
            accOnScene.accountNumberProperty().set("");
            accOnScene.setRemark("");
            backupObj.copyFrom(accOnScene);
        }
    }

    @Override
    protected void removeBinds(){
        Account accOnScene  = (Account)sceneObj;
        Bindings.unbindBidirectional(accOnScene.clientIdProperty(), clients.valueProperty());
        Bindings.unbindBidirectional(accOnScene.balAccountIdProperty(), balAccounts.valueProperty());
        accOnScene.balAccountProperty().unbind();
    }
    
    @Override
    protected void removeListeners(){
        
    }
    
    /**
     *  The class provide to convert Client to its recId as String and vice verse - recId to appropriate Client from comboBox.
     */
    private class ClientToIdBiConverter extends StringConverter<Client> {
        
        @Override
        public String toString(Client client) {
            String result = null;
            if (client != null) {
                result = "" + client.getRecId();
            }
            return result;
        }

        @Override
        public Client fromString(String recId) {
            Client result = null;
            Optional<Client> optClient = clients.getItems().stream().filter(client -> ("" + client.getRecId()).equals(recId)).findFirst();
            if (optClient.isPresent()){
                result = optClient.get();
            }
            return result;
        }
    }
    
    /**
     *  The class provide to convert BalanceAccount to its recId as String and vice verse - recId to appropriate BalanceAccount from comboBox.
     */
    private class BalAccountToIdBiConverter extends StringConverter<BalanceAccount> {

        @Override
        public String toString(BalanceAccount balAcc) {
            String result = null;
            if (balAcc != null){
                result = "" + balAcc.getRecId();
            }
            return result;
        }

        @Override
        public BalanceAccount fromString(String recId) {
            BalanceAccount result = null;
            Optional<BalanceAccount> optBalAcc = balAccounts.getItems().stream().filter(balanceAccount -> ("" + balanceAccount.getRecId()).equals(recId)).findFirst();
            if (optBalAcc.isPresent()){
                result = optBalAcc.get();
            }
            return result;
        }
        
    }
    
    
    private class CustomNumberGenerator {

        private final String procedureNameForKey = "account_set_key";
        private final String procedureNameForNew = "account_insert_update_get_fit_account";
        
        public final String accountNumberJsonKey = "account";
        public final String accountExistsJsonKey = "accountExists";
        public final String accountInOtherISOJsonKey = "accountInOtherIso";
        public final String accountNewJsonKey = "accountNew";
        public final String fitFlagJsonKey = "fitFlag";
        public final String accountDescripJsonKey = "descrip";
        
        private final DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        private final String emptyServerResponse = "No account number";
        private int fitFlag = 0;
        
        public void generateKeyFor(Consumer<String> success, Consumer<Exception> error) {
            new Thread(() -> {
                try {
                    Integer accountNum = (!accountNumber.getText().isEmpty()) ? Integer.parseInt(accountNumber.getText()) : null;
                    String accNum = getAccountNumber(accountNum);
                    Platform.runLater(() -> {
                        if (success != null) success.accept(accNum);
                    });
                } catch (AuthServerException | IOException | NumberFormatException | JSONException ex) {
                    Platform.runLater(() -> {
                        if (error != null) error.accept(ex);
                    });
                }
            }).start();
        }
        
        private String getAccountNumber(Integer accNumWithoutKey) throws IOException, AuthServerException, JSONException {
            JSONArray data = dbClient.callProcedureAndGetAsJson(procedureNameForKey, accNumWithoutKey);
            System.out.println("getAccountNumber data: " + data);
            return (!data.isNull(0)) ? "" + data.getJSONObject(0).optInt(accountNumberJsonKey) : emptyServerResponse;
        }

        public void generateNewNumber(Consumer<JSONObject> success, Consumer<Exception> error,
                                        Integer accNum, Integer clientId, int balAcc, String iso) {
            new Thread(() -> {
                try {
                    JSONArray data = dbClient.callProcedureAndGetAsJson(procedureNameForNew, accNum, clientId, balAcc, iso, fitFlag);
                    System.out.println("account data: " + data);
                    if (!data.isNull(0)){
                        JSONObject obj = data.getJSONObject(0);
                        Platform.runLater(() -> {
                            if (success != null) success.accept(obj);
                        });
                    }
                } catch (AuthServerException | IOException | JSONException  ex) {
                    Platform.runLater(() -> {
                        if (error != null) error.accept(ex);
                    });
                }
                
            }).start();
        }
        
        public String getWarningMessageFrom(JSONObject obj){
            String key = "";
            if (obj.has(accountExistsJsonKey)){
                key = obj.optString(accountExistsJsonKey);
            }
            else if (obj.has(accountInOtherISOJsonKey)){
                key = obj.optString(accountInOtherISOJsonKey);
            }
            else if (obj.has(accountNewJsonKey)){
                key = obj.optString(accountNewJsonKey);
            }
            return GeneralConfig.getInstance().getTitleFor(key);
        }
        
        public void resetFitFlag(){
            fitFlag = 0;
        }
        
        public void setFitFlag(int value){
            fitFlag = value;
        }
    }
}
