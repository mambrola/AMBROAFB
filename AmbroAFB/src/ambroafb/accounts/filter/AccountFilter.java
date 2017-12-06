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
        
        fetcherInThreadCount = 3;
        okayCancelController.setOkayDisable(true);
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
        Consumer<ObservableList<Currency>> selectCurrency = (currencyList) -> {
            Optional<Currency> optCurrency = currencyList.stream().filter((curr) -> curr.getIso().equals(model.getCurrencyIso())).findFirst();
            if (optCurrency.isPresent()){
                currencies.setValue(optCurrency.get());
            }
        };
        Consumer<ObservableList<Currency>> increaseFromCurrency = (balAccList) -> {
            increaseCounter.accept(null);
        };
        currencies.fillComboBoxWithALL(selectCurrency.andThen(increaseFromCurrency));
        
        Consumer<ObservableList<BalanceAccount>> selectBalAcc = (balAccList) -> {
            Optional<BalanceAccount> optBallAcc = balAccList.stream().filter((balAcc) -> balAcc.getBalAcc() == model.getBalAccountId()).findFirst();
            if (optBallAcc.isPresent()){
                balAccounts.setValue(optBallAcc.get());
            }
        };
        Consumer<ObservableList<BalanceAccount>> increaseFromBalAcc = (balAccList) -> {
            increaseCounter.accept(null);
        };
        balAccounts.fillComboBoxWithALL(selectBalAcc.andThen(increaseFromBalAcc));

        Consumer<ObservableList<Client>> clientConsumer = (clientList) -> {
            Optional<Client> optClient = clientList.stream().filter((client) -> client.getRecId() == model.getClientId()).findFirst();
            if (optClient.isPresent()){
                clients.getSelectionModel().select(optClient.get());
            }
        };
        Consumer<ObservableList<Client>> increaseFromClient = (clientList) -> {
            increaseCounter.accept(null);
        };
        clients.fillComboBoxWithClientsAndPartnersWithALL(clientConsumer.andThen(increaseFromClient));

        accountType.setSelected(model.isTypeSelected());
        accountType.setIndeterminate(model.getTypeIndeterminate());
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
