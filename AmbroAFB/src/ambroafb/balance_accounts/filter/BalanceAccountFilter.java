/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.filter;

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
public class BalanceAccountFilter extends Stage implements Filterable, Initializable {

    private final JSONObject json;
    
    public BalanceAccountFilter(Stage owner){
        String currStagePath = Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH;
        Utils.saveShowingStageByPath(currStagePath, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
//        this.setTitle(GeneralConfig.getInstance().getTitleFor("bal_account_filter"));
        Scene scene = Utils.createScene("/ambroafb/balance_accounts/filter/BalanceAccountFilter.fxml", (BalanceAccountFilter)this);
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
