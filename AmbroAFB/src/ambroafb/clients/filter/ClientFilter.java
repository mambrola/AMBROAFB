/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambro.ADatePicker;
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
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable, Initializable {
    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private JSONObject jSonResult;
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public ClientFilter(Stage owner) {
        String clientFilterPath = Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH;
        Utils.saveShowingStageByPath(clientFilterPath, (Stage)this);
        
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
            
            JSONObject baseJS = new JSONObject();
            baseJS.put("dateBigger", (dateBigger.getValue() == null) ? "" : dateBigger.getValue());
            baseJS.put(  "dateLess", (  dateLess.getValue() == null) ? "" :   dateLess.getValue());
            
            UtilsDB.getInstance().updateOrInsertDefaultParameters("clients", "filter", baseJS);
        } catch (JSONException ex) { Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            JSONObject json = UtilsDB.getInstance().getDefaultParametersJson("clients", "filter");
            if (json != null && json.length() > 0){
                String dateB = json.getString("dateBigger");
                String dateL = json.getString(  "dateLess");
                
                LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
                LocalDate less   = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);
 
                dateBigger.setValue(bigger);
                dateLess.setValue(less);
            } 
        }
        catch (JSONException ex) {
            Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
