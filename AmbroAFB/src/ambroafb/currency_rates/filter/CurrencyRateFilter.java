/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.interfaces.UserInteractiveStage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyRateFilter extends UserInteractiveStage implements Filterable, Initializable {

    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final CurrencyRateFilterModel currencyRateFilterModel = new CurrencyRateFilterModel();
    
    public CurrencyRateFilter(Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "currency_rates", "/images/filter.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/filter/CurrencyRateFilter.fxml", (CurrencyRateFilter)this);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
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
        dateBigger.setValue(currencyRateFilterModel.getFromDate());
        dateLess.setValue(currencyRateFilterModel.getToDate());
    }    
    
}
