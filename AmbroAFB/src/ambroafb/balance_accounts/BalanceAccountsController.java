/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
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
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.MaskerPane;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class BalanceAccountsController implements Initializable {

    @FXML
    private AFilterableTreeTableView<EditorPanelable> aview; // this name is need for editorPanel
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private EditorPanelController editorPanelController;
            
    
    private final ObservableList<BalanceAccount> roots = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTreeTable(aview);
        reAssignTable(null);
    }

    public void reAssignTable(JSONObject filterJson){
        new Thread(new BalanceAccountsFromDB(roots)).start();
    }
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
    
    public boolean accountAlreadyExistForCode(String balAccCode){
        boolean result = false;
        for (BalanceAccount root : roots) {
            result = result || existAccount(root, Integer.parseInt(balAccCode));
        }
        return result;
    }
    
    private boolean existAccount(BalanceAccount account, int code){
        boolean result = false;
        if (account.getBalAcc() == code)
            result = true;
        else{
            for (BalanceAccount childAccount : account.childrenAccounts) {
                result = result || existAccount(childAccount, code);
            }
        }
        return result;
    }
    
    public boolean accountHasParent(String balAccCode){
        boolean result = false;
        for (BalanceAccount account : roots) {
            result = result || containsChildForCode(account, balAccCode);
        }
        return result;
    }
    
    private boolean containsChildForCode(BalanceAccount account, String code){
        boolean result = false;
        String accounCode = "" + account.getBalAcc();
        if (isDirectChildNode(accounCode, code)) 
            result = true;
        else{
            for (BalanceAccount childAccount : account.childrenAccounts) {
                result = result || containsChildForCode(childAccount, code);
            }
        }
        return result;
    }
    
    private boolean isDirectChildNode(String accountCode, String code){
        String commonPrefix = StringUtils.getCommonPrefix(accountCode, code);
        String currAccCodeAfterPrefix = StringUtils.substringAfter(accountCode, commonPrefix);
        String searchCodeAfterPrefix = StringUtils.substringAfter(code, commonPrefix);
        return currAccCodeAfterPrefix.length() > 1 && 
               currAccCodeAfterPrefix.charAt(0) == '0' && 
               searchCodeAfterPrefix.charAt(0) != '0';
    }
    
    
    private class BalanceAccountsFromDB implements Runnable {

        private final ObservableList<BalanceAccount> roots;
        private final Map<Integer, BalanceAccount> items = new HashMap<>();
        
        public BalanceAccountsFromDB(ObservableList<BalanceAccount> roots){
            this.roots = roots;
        }
        
        @Override
        public void run() {
            Platform.runLater(() -> {
                masker.setVisible(true); 
            });
            BalanceAccount.getAllFromDB().stream().forEach((account) -> {
                makeTreeStructure(account);
            });
            Platform.runLater(() -> {
                roots.stream().forEach((account) -> {
                    aview.append(account);
                });
                aview.expand(1);
                masker.setVisible(false); 
            });
        }
        
        
        private void makeTreeStructure(BalanceAccount account) {
            int accountCode = account.getBalAcc();
            items.put(accountCode, account);
            if (accountCode % 1000 == 0) {
                    account.rowStyle.add("font" + 8 + "Size");
                    roots.add(account);
            } else {
                int reminder;
                if (accountCode % 100 == 0) {
                    reminder = accountCode % 1000;
                    account.rowStyle.add("font" + 4 + "Size");
                } else {
                    account.rowStyle.add("font" + 2 + "Size");
                    reminder = accountCode % 100;
                }
                BalanceAccount parentAccount = getParentAccount(accountCode, reminder);
                if (parentAccount != null) {
                    parentAccount.childrenAccounts.add(account);
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
