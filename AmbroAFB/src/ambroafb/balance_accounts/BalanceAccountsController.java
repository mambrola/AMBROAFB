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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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
        treeTable.getColumns().add(new TreeTableColumn<>("Column"));
        reAssignTable(null);
    }

    public void reAssignTable(JSONObject filterJson){
        new Thread(new BalanceAccountsFromDB()).start();
    }
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
    
    
    private class BalanceAccountsFromDB implements Runnable {

        @Override
        public void run() {
            Platform.runLater(() -> {
                masker.setVisible(true); 
            });
            BalanceAccount.getAllFromDBTest().stream().forEach((account) -> {
                addAcountIntoTree(account);
            });
            Platform.runLater(() -> {
                masker.setVisible(false); 
            });
        }
        
        
        private void addAcountIntoTree(BalanceAccount account) {
            int accountCode = account.getCode();
            if (accountCode % 1000 == 0) {
                Platform.runLater(() -> {
                    treeTable.append(account);
                    System.out.println("descrip: " + account.getDescrip());
                });
            } else {
                int reminder = 0;
                if (accountCode % 100 == 0) {
                    reminder = accountCode % 1000;
                } else if (accountCode % 10 == 0) {
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
            for (TreeItem<EditorPanelable> treeItem : treeTable.getRoot().getChildren()) {
                BalanceAccount account = (BalanceAccount) treeItem.getValue();
                if (account.getCode() + currAccountCodeReminder == currAcountCode) {
                    result = account;
                }
            }
            return result;
        }
    }
}
