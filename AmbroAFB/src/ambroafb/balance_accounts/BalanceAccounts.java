/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.Utils;
import ambroafb.general.StageUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class BalanceAccounts extends Stage {
    
    private BalanceAccountsController balanceAccountsController;
    
    public BalanceAccounts(Stage owner){
        Utils.registerStageByOwner(owner, BalanceAccounts.class.getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/balance_accounts/BalanceAccounts.fxml", null);
        balanceAccountsController = (BalanceAccountsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            balanceAccountsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        Utils.setSizeFor((Stage)this);
    }
    
    public BalanceAccountsController getCountriesController(){
        return balanceAccountsController;
    }
}
