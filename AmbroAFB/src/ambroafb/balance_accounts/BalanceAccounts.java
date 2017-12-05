/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.ListingController;
import ambroafb.general.interfaces.ListingStage;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class BalanceAccounts extends ListingStage {
    
    public BalanceAccounts(Stage owner, Class tableContent, String stageTitleBundleKey){
        this(owner, tableContent, stageTitleBundleKey, new StandardEditorPanel());
    }
    
    public BalanceAccounts(Stage owner, Class tableContent, String stageTitleBundleKey, EditorPanel editorPanel){
        super(owner, "/ambroafb/balance_accounts/BalanceAccounts.fxml", tableContent, stageTitleBundleKey, editorPanel);
    }

    @Override
    public ListingController getController() {
        return controller;
    }
    
}
