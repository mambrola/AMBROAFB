/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyRateFilter extends UserInteractiveFilterStage implements Filterable, Initializable {

    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final CurrencyRateFilterModel currencyRateFilterModel = new CurrencyRateFilterModel();
    
    public CurrencyRateFilter(Stage owner){
        super(owner, "currency_rates");
        
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/filter/CurrencyRateFilter.fxml", (CurrencyRateFilter)this);
        this.setScene(scene);
    }
    
    @Override
    public FilterModel getResult() {
        showAndWait();
        return currencyRateFilterModel;
    }
    

    @Override
    public void setResult(boolean isOk){
        if(!isOk){
            currencyRateFilterModel.changeModelAsEmpty();
        }
        else {
            dateBigger.setEditingValue();
            dateLess.setEditingValue();
            
            currencyRateFilterModel.setFromDate(dateBigger.getValue());
            currencyRateFilterModel.setToDate(dateLess.getValue());
            currencyRateFilterModel.setSelectedCurrency(currencies.getValue());
        }
    }
    
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate monthBeforeNow = LocalDate.now().minusMonths(1);
        dateBigger.setValue(monthBeforeNow);
        dateLess.setValue(null);
        
        Consumer<ObservableList<Currency>> currencyConsumer = (currencyList) -> {
            Optional<Currency> optCurrency = currencyList.stream().filter((curr) -> curr.getIso().equals(currencyRateFilterModel.getSelectedCurrencyIso())).findFirst();
            if (optCurrency.isPresent()){
                currencies.setValue(optCurrency.get());
            }
        };
        currencies.fillComboBoxWithALLAndWithoutRatesBasicIso(currencyConsumer);
    }    

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
