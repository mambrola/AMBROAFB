/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.filter;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.balance_accounts.BalanceAccountTreeComboBox;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class AccountFilter extends UserInteractiveFilterStage implements Filterable, Initializable {

    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private BalanceAccountTreeComboBox balAccounts;
    @FXML
    private ClientComboBox clients;
    @FXML
    private CheckBox accountType;
    @FXML
    private FilterOkayCancelController okayCancelController;
            
    private final AccountFilterModel model = new AccountFilterModel();
    
    public AccountFilter(Stage owner){
        super(owner, "accounts");
        
        Scene scene = SceneUtils.createScene("/ambroafb/accounts/filter/AccountFilter.fxml", (AccountFilter)this);
        this.setScene(scene);
    }
    
    @Override
    public FilterModel getResult() {
        showAndWait();
        return model;
    }

    @Override
    public void setResult(boolean isOk) {
        if (!isOk){
            model.changeModelAsEmpty();
        }
        else {
            model.setCurrency(currencies.getValue());
            model.setBalAccount(balAccounts.getValue());
            model.setClient(clients.getValue());
            model.setTypeIndeterminate(accountType.isIndeterminate());
            model.setType(accountType.isSelected());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Consumer<ObservableList<Currency>> currencyConsumer = (currencyList) -> {
            Optional<Currency> optCurrency = currencyList.stream().filter((curr) -> curr.getIso().equals(model.getCurrencyIso())).findFirst();
            if (optCurrency.isPresent()){
                currencies.setValue(optCurrency.get());
            }
        };
        currencies.fillComboBoxWithALL(currencyConsumer);
        
        Consumer<ObservableList<BalanceAccount>> balAccountConsumer = (balAccList) -> {
            Optional<BalanceAccount> optBallAcc = balAccList.stream().filter((balAcc) -> balAcc.getBalAcc() == model.getBalAccountNumber()).findFirst();
            if (optBallAcc.isPresent()){
                balAccounts.setValue(optBallAcc.get());
            }
        };
        balAccounts.fillComboBoxWithALL(balAccountConsumer);

        Consumer<ObservableList<Client>> clientConsumer = (clientList) -> {
            Optional<Client> optClient = clientList.stream().filter((client) -> client.getRecId() == model.getClientId()).findFirst();
            if (optClient.isPresent()){
                clients.getSelectionModel().select(optClient.get());
            }
        };
        clients.fillComboBoxWithClientsAndPartnersWithALL(clientConsumer);

        accountType.setSelected(model.isTypeSelected());
        accountType.setIndeterminate(model.getTypeIntdeterminate());
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
