/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.UtilsDB;
import ambroafb.general.interfaces.Filterable;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable, Initializable{
    @FXML
    private DatePicker dateBiger, dateLess;
    @FXML
    private Button cancel;
    
    private JSONObject jSonResult;
    
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public ClientFilter(Stage owner) {
        String ownerPath = Utils.getPathForStage(owner);
        String clientFilterPath = ownerPath + Filterable.LOCAL_NAME;
        Utils.saveShowingStageByPath(clientFilterPath, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("clients_filter"));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setResources(GeneralConfig.getInstance().getBundle());
            fxmlLoader.setController((ClientFilter)this);
            Scene scene = new Scene(fxmlLoader.load(AmbroAFB.class.getResource("/ambroafb/clients/filter/ClientFilter.fxml").openStream()));
            scene.getProperties().put("controller", fxmlLoader.getController());
            this.setScene(scene);
        } catch (IOException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
        this.initOwner(owner);
        setResizable(false);
    }

    @Override
    public JSONObject getResult() {
        showAndWait();
        return jSonResult;
    }

    @Override
    public void setResult(boolean isOk){
        if(!isOk)
            return;
        jSonResult = new JSONObject();
        try {
            jSonResult.put("dateBigger", (dateBiger.getValue() == null ? DATE_BIGGER : dateBiger.getValue()).toString());
            jSonResult.put("dateLess", (dateLess.getValue() == null ? DATE_LESS : dateLess.getValue()).toString());
            
            JSONObject baseJS = new JSONObject();
            baseJS.put("dateBigger", (dateBiger.getValue() == null) ? "" : dateBiger.getValue());
            baseJS.put("dateLess", (dateLess.getValue() == null) ? "" : dateLess.getValue());
            
            UtilsDB.getInstance().updateFilters("clients", "filter", baseJS);
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //dateBiger.setValue(LocalDate.MIN);
        // იღებს derby ბაზიდან dateBiger, dateLess-ების მნიშვნელობებს
        try {
            JSONObject json = UtilsDB.getInstance().getFilterJson("clients", "filter");
            if (json != null && json.length() > 0){
                String dateB = json.getString("dateBigger");
                String dateL = json.getString("dateLess");
                
                LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
                LocalDate less   = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);
 
                dateBiger.setValue(bigger);
                dateLess.setValue(less);
            } 
        }
        catch (JSONException ex) {
            Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
