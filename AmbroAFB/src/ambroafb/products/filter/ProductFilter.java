/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.filter;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Filterable;
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
public class ProductFilter extends Stage implements Filterable, Initializable {

    private final JSONObject json;
    
    public ProductFilter(Stage owner){
        String productFilterPath = Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH;
        System.out.println("shevinaxet: path: " + productFilterPath + "  class: " + getClass().getSimpleName() + " stage: " + (Stage)this);
        Utils.saveShowingStageByPath(productFilterPath, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("products_filter"));
        Scene scene = Utils.createScene("/ambroafb/products/filter/ProductFilter.fxml", (ProductFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);
        
        json = new JSONObject();
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
