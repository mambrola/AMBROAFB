/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.SceneUtils;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
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
        StagesContainer.registerStageByOwner(owner, BalanceAccounts.class.getSimpleName(), (Stage)this);
        
        Scene scene = SceneUtils.createScene("/ambroafb/balance_accounts/BalanceAccounts.fxml", null);
        balanceAccountsController = (BalanceAccountsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            balanceAccountsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> balanceAccountsController.getEditorPanelController().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public BalanceAccountsController getCountriesController(){
        return balanceAccountsController;
    }
}
