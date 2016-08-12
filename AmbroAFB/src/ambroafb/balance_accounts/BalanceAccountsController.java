/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.ATreeTableView;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.MaskerPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class BalanceAccountsController implements Initializable {

    @FXML
    private ATreeTableView<EditorPanelable> treeTable;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private EditorPanelController editorPanelController;
            
    
//    private final ObservableList<EditorPanelable> accounts = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.removeButtonsByFxIDs("#delete", "#edit", "#view", "#add", "#refresh");
        reAssignTable(null);
    }

    public void reAssignTable(JSONObject filterJson){
        new Thread(new BalanceAccountsFromDB()).start();
    }
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
    
    
    private class BalanceAccountsFromDB implements Runnable {

        private final ObservableList<BalanceAccount> roots = FXCollections.observableArrayList();
        private final Map<Integer, BalanceAccount> items = new HashMap<>();
        
        @Override
        public void run() {
            Platform.runLater(() -> {
                masker.setVisible(true); 
            });
            BalanceAccount.getAllFromDBTest().stream().forEach((account) -> {
                makeTreeStructure(account);
            });
            Platform.runLater(() -> {
                roots.stream().forEach((account) -> {
                    treeTable.append(account);
                });
                masker.setVisible(false); 
            });
        }
        
        
        private void makeTreeStructure(BalanceAccount account) {
            int accountCode = account.getCode();
            if (accountCode % 10 != 0) return;
            items.put(accountCode, account);
            if (accountCode % 1000 == 0) {
                    account.rowStyle.add("font" + 8 + "Size");
                    roots.add(account);
            } else {
                int reminder = 0;
                if (accountCode % 100 == 0) {
                    reminder = accountCode % 1000;
                    account.rowStyle.add("font" + 4 + "Size");
                } else if (accountCode % 10 == 0) {
                    account.rowStyle.add("font" + 2 + "Size");
                    reminder = accountCode % 100;
                }
                BalanceAccount parentAccount = getParentAccount(accountCode, reminder);
                if (parentAccount != null) {
                    parentAccount.addChildAccount(account);
                }
            }
        }
        
        private BalanceAccount getParentAccount(int currAcountCode, int currAccountCodeReminder) {
            BalanceAccount result = null;
            if (items.containsKey(currAcountCode - currAccountCodeReminder)){
                result = items.get(currAcountCode - currAccountCodeReminder);
            }
            return result;
        }
    }
}
