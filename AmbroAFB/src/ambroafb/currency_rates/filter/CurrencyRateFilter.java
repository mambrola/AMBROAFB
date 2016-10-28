/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyRateFilter extends Stage implements Filterable, Initializable {

    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private JSONObject jSonResult;
    
    private final CurrencyRateFilterModel currencyRateFilterModel = new CurrencyRateFilterModel();
    
    public CurrencyRateFilter(Stage owner){
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        Scene scene = SceneUtils.createScene("/ambroafb/currency_rates/filter/CurrencyRateFilter.fxml", (CurrencyRateFilter)this);
        setStageFeatures(scene, owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
    }
    
    private void setStageFeatures(Scene scene, Stage owner){
        this.setScene(scene);
        this.initOwner(owner);
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("currency_rate_filter"));
        this.setResizable(false);
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
            currencyRateFilterModel.setSelectedCurrencyIndex(currencies.getSelectionModel().getSelectedIndex());
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
        
        // Note: currencies.setValue(...); will be incorrect, when open this stage again getSelectedCurrency will be null. We read value from pref.
        currencies.getSelectionModel().select(currencyRateFilterModel.getSelectedCurrencyIndex());
    }    
    
}
