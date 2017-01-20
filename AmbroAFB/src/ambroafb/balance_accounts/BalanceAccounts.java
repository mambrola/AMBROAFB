/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class BalanceAccounts extends ListingStage {
    
    private BalanceAccountsController balanceAccountsController;
    
    public BalanceAccounts(Stage owner){
        super(owner, StringUtils.substringAfterLast(BalanceAccounts.class.toString(), "."),  "balances", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/balance_accounts/BalanceAccounts.fxml", null);
        balanceAccountsController = (BalanceAccountsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            balanceAccountsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> balanceAccountsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public BalanceAccountsController getCountriesController(){
        return balanceAccountsController;
    }
}
