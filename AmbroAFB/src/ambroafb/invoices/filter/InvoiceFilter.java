/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.filter;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.save_button.StageUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class InvoiceFilter extends Stage implements Filterable, Initializable {

    private final JSONObject json;
    
    public InvoiceFilter(Stage owner){
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("invoices_filter"));
        Scene scene = Utils.createScene("/ambroafb/invoices/filter/InvoiceFilter.fxml", (InvoiceFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);
        
        json = new JSONObject();
        
        StageUtils.centerChildOf(owner, (Stage)this);
    }
    
    @Override
    public JSONObject getResult() {
        return json;
    }

    @Override
    public void setResult(boolean isOk) {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
