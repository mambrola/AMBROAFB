/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.currency_rates.CurrencyRatesComboBox;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.UtilsDB;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONException;
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
    private CurrencyRatesComboBox currRatesComboBox;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private JSONObject jSonResult;
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public CurrencyRateFilter(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("currency_rate_filter"));
        Scene scene = Utils.createScene("/ambroafb/currency_rates/filter/CurrencyRateFilter.fxml", (CurrencyRateFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
        okayCancelController.disableProperty().bind(currRatesComboBox.disableProperty());
    }
    
    @Override
    public JSONObject getResult() {
        showAndWait();
        System.out.println("getResult");
        return jSonResult;
    }

    @Override
    public void setResult(boolean isOk){
        jSonResult = new JSONObject();
        if(!isOk)
            return;
        try {
            dateBigger.setEditingValue();
            dateLess.setEditingValue();
            
            jSonResult.put("dateBigger", (dateBigger.getValue() == null ? DATE_BIGGER : dateBigger.getValue()).toString());
            jSonResult.put(  "dateLess", (  dateLess.getValue() == null ? DATE_LESS   :   dateLess.getValue()).toString());
            jSonResult.put("currency", (currRatesComboBox.getValue() == null)? "" : currRatesComboBox.getValue());
            
            JSONObject baseJS = new JSONObject();
            baseJS.put("dateBigger", (dateBigger.getValue() == null) ? "" : dateBigger.getValue());
            baseJS.put(  "dateLess", (  dateLess.getValue() == null) ? "" :   dateLess.getValue());
            baseJS.put("currency", (currRatesComboBox.getValue() == null)? "" : currRatesComboBox.getValue());
            
            UtilsDB.getInstance().updateOrInsertDefaultParameters("currency_rate", "filter", baseJS);
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            JSONObject json = UtilsDB.getInstance().getDefaultParametersJson("currency_rate", "filter");
            if (json != null && json.length() > 0){
                String dateB = json.getString("dateBigger");
                String dateL = json.getString(  "dateLess");
                String currency = json.getString("currency");
                
                LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
                LocalDate less   = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);
 
                dateBigger.setValue(bigger);
                dateLess.setValue(less);
                currRatesComboBox.setValue(currency);
//                currRatesComboBox.changeValue(currency);
            } 
        }
        catch (JSONException ex) {
            Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
