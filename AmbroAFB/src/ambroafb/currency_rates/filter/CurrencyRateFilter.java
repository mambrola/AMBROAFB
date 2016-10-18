/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.clients.filter.ClientFilter;
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
    private CurrencyComboBox currencies;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private JSONObject jSonResult;
    private static final String DATE_BIGGER = "1970-01-01";
    private static final String DATE_LESS = "9999-01-01";
    
    private static final String PREF_BIGGER_DATE = "currency_rate/filter/dateBigger";
    private static final String PREF_LESS_DATE = "currency_rate/filter/dateLess";
    private static final String PREF_CURRENCY = "currency_rate/filter/currency";
    
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
    public JSONObject getResult() {
        showAndWait();
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
            jSonResult.put(  "currency", (currencies.getValue() == null)? "" : currencies.getValue().getIso());
            
            GeneralConfig.prefs.put(PREF_BIGGER_DATE, (dateBigger.getValue() == null) ? "" : dateBigger.getValue().toString());
            GeneralConfig.prefs.put(PREF_LESS_DATE, (  dateLess.getValue() == null) ? "" :   dateLess.getValue().toString());
            GeneralConfig.prefs.put(PREF_CURRENCY, (currencies.getValue() == null)? "" : currencies.getValue().getIso());
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String dateB = GeneralConfig.prefs.get(PREF_BIGGER_DATE, "");
        String dateL = GeneralConfig.prefs.get(PREF_LESS_DATE, "");
        String currency = GeneralConfig.prefs.get(PREF_CURRENCY, "");
        
        LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
        LocalDate less   = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);

        dateBigger.setValue(bigger);
        dateLess.setValue(less);
        currencies.changeValue(currency);
        
    }    

    @Override
    public FilterModel getFilterResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
