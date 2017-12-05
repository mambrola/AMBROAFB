/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances.filter;

import ambro.ADatePicker;
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
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class BalanceFilter extends UserInteractiveFilterStage implements Filterable, Initializable {
    
    @FXML
    private ADatePicker date;
    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final BalanceFilterModel model = new BalanceFilterModel();
    
    public BalanceFilter(Stage owner){
        super(owner, "balances");
        
        Scene scene = SceneUtils.createScene("/ambroafb/balances/filter/BalanceFilter.fxml", (BalanceFilter)this);
        this.setScene(scene);
        
        comboBoxCount = 1;
        okayCancelController.setOkayDisable(true);
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
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
            model.setDate(date.getValue());
            model.setCurrency(currencies.getValue());
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
        currencies.fillComboBoxWithoutALL(selectCurrency.andThen(increaseFromCurrency));
        
        date.setValue(model.getDate());
    }
    
}
 