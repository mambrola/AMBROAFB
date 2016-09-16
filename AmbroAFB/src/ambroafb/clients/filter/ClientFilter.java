/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.countries.CountryComboBox;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable, Initializable {
    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private CountryComboBox countries;
    @FXML
    private CheckComboBox<String> statuses;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    
    private JSONObject jSonResult;
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public ClientFilter(Stage owner) {
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        Utils.centerStageOfParent((Stage)this, owner);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("clients_filter"));
        Scene scene = Utils.createScene("/ambroafb/clients/filter/ClientFilter.fxml", (ClientFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        statuses.getItems().setAll(Client.getStatuses());
        
        countries.setValue(countries.getItems().get(0));
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
            jSonResult.put( "juridical", (  juridical.indeterminateProperty().get() ? 2 : juridical.isSelected() ? 1 : 0 ));
            jSonResult.put(   "country",  countries.getValue());
//            jSonResult.put(    "status",   statuses.getCheckModel().get);
            jSonResult.put(  "rezident", (  rezident.indeterminateProperty().get() ? 2 : rezident.isSelected() ? 1 : 0 ));
            
            GeneralConfig.prefs.put("clients/filter/dateBigger", (dateBigger.getValue() == null) ? "" : dateBigger.getValue().toString());
            GeneralConfig.prefs.put(  "clients/filter/dateLess", (  dateLess.getValue() == null) ? "" :   dateLess.getValue().toString());
            GeneralConfig.prefs.putInt( "clients/filter/juridical", jSonResult.getInt("juridical"));
            Country country = countries.getValue();
            GeneralConfig.prefs.putInt(   "clients/filter/country/rec_id", (country == null) ? 0 : country.getRecId());
            GeneralConfig.prefs.put(   "clients/filter/country/code", (country == null) ? "" : country.getCode());
            GeneralConfig.prefs.put(   "clients/filter/country/descrip", (country == null) ? "" : country.getDescrip());
//            GeneralConfig.prefs.put(   "clients/filter/status", jSonResult.getString("status"));
            GeneralConfig.prefs.putInt(   "clients/filter/rezident", jSonResult.getInt("rezident"));
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String dateB = GeneralConfig.prefs.get("clients/filter/dateBigger", "");
        String dateL = GeneralConfig.prefs.get("clients/filter/dateLess", "");
        int jurid = GeneralConfig.prefs.getInt("clients/filter/juridical", 0);
        int countryId = GeneralConfig.prefs.getInt("clients/filter/country/rec_id", -1);
        String code = GeneralConfig.prefs.get("clients/filter/country/code", "");
        String countryDescrip = GeneralConfig.prefs.get("clients/filter/country/descrip", "");
        Country country = new Country(code, countryDescrip);
        country.setRecId(countryId);
        
        String status = GeneralConfig.prefs.get("clients/filter/status", null);
        int rez = GeneralConfig.prefs.getInt("clients/filter/rezident", 0);

        LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
        LocalDate less = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);

        dateBigger.setValue(bigger);
        dateLess.setValue(less);
        juridical.setSelected(jurid == 1);
        juridical.setIndeterminate(jurid == 2);
        if (countryId != -1)
            countries.setValue(country);
//        statuses.setValue(status);
        rezident.setSelected(rez == 1);
        rezident.setIndeterminate(rez == 2);
    }
    
}
