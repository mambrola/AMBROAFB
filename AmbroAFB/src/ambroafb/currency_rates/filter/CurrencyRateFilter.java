/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambro.ADatePicker;
import ambroafb.clients.filter.ClientFilter;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
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
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public CurrencyRateFilter(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        // Center of its parent:
        this.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            setX(owner.getX() + owner.getWidth() / 2 - getWidth() / 2);
        });
        this.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            setY(owner.getY() + owner.getHeight()/ 2 - getHeight() / 2);
        });
        
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
        currencies.removeCurrency("GEL");
        System.out.println("konstruqtori");
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
            jSonResult.put("currency", (currencies.getValue() == null)? "" : currencies.getValue().getIso());
            
            GeneralConfig.prefs.put("currency_rate/filter/dateBigger", (dateBigger.getValue() == null) ? "" : dateBigger.getValue().toString());
            GeneralConfig.prefs.put("currency_rate/filter/dateLess", (  dateLess.getValue() == null) ? "" :   dateLess.getValue().toString());
            GeneralConfig.prefs.put("currency_rate/filter/currency", (currencies.getValue() == null)? "" : currencies.getValue().getIso());
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String dateB = GeneralConfig.prefs.get("currency_rate/filter/dateBigger", "");
        String dateL = GeneralConfig.prefs.get("currency_rate/filter/dateLess", "");
        String currency = GeneralConfig.prefs.get("currency_rate/filter/currency", "");

        LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
        LocalDate less   = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);

        dateBigger.setValue(bigger);
        dateLess.setValue(less);
        currencies.changeValue(currency);
        
        System.out.println("initialize. currency: " + currency);
    }    
    
}
