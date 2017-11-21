/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class BalanceAccountsController extends ListingController {

    @FXML
    private AFilterableTreeTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<BalanceAccount> roots = FXCollections.observableArrayList();
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        aview.getColumns().stream().forEach((column) -> {
            column.setSortable(false);
        });
    }

    
    @Override
    public void reAssignTable(FilterModel model) {
        roots.clear();
        aview.removeAll();
        new Thread(new BalanceAccountsRunnable(model)).start();
    }
    
    @Override
    public void addListWith(Class content) {
        aview.initialize(content);
//        editorPanel.buttonsMainPropertiesBinder(aview);
        editorPanel.setTreeTable(aview);
        
        aview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<EditorPanelable>> observable, TreeItem<EditorPanelable> oldValue, TreeItem<EditorPanelable> newValue) -> {
            if (newValue != null && newValue.getValue() != null){
                observers.stream().forEach((observer) -> observer.notify(newValue.getValue()));
            }
        });
    }
    
    /**
     * The method checks already exist account in tree or not. The logic is following:
     * If code is the same, then calls BalanceAccount compares(...) method.
     * If compare(...) method return false, it means that user want to edit account 
     * with existed code. So the method return true.
     * If compare(...) method return true, it means that account which was found in tree and user editable 
     * account are the same. So the method return false.
     * @param newElem BalanceAccount as EditorPanelable object that must be search.
     * @param type DELETE, EDIT, VIEW, or ADD.
     * @return 
     */
    public boolean accountAlreadyExistForCode(EditorPanelable newElem, EditorPanel.EDITOR_BUTTON_TYPE type){
        BalanceAccount newAccount = (BalanceAccount) newElem;
        boolean result = false;
        for (BalanceAccount currAcount : roots) {
            result = result || existAccount(currAcount, newAccount, type);
        }
        return result;
    }
    
    /**
     * The method checks already exist account in tree or not. The logic is following:
     * If method must work in edit case, then it compares treeItems values to the given search
     * BalanceAccount. If code is the same, then calls BalanceAccount compares() method.
     * If compare(...) method return false, it means that user want to edit account with existed code.
     * So the method return true.
     * If compares return true, it means that account which was found in tree and user editable 
     * account are the same.
     * So the method return false.
     * @param account Current account in recursion.
     * @param search Account which must be find.
     * @param type DELETE, EDIT, VIEW, or ADD.
     * @return 
     */
    private boolean existAccount(BalanceAccount account, BalanceAccount search, EditorPanel.EDITOR_BUTTON_TYPE type){
        List<Boolean> valuesComparesResults = new ArrayList<>();
        if (account.getBalAcc() == search.getBalAcc()){
            if (type.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD)) return true;
            valuesComparesResults.add(account.compares(search));
        }
        for (BalanceAccount childAccount : account.childrenAccounts) {
            if (existAccount(childAccount, search, type)){
                if (type.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD)) return true;
                valuesComparesResults.add(false);
            }
        }
        for (Boolean result : valuesComparesResults) {
            if (!result) return true;
        }
        return false;
    }
    
    /**
     * The method finds parent account for the given account code.
     * @param balAccCode
     * @return 
     */
    public boolean accountHasParent(String balAccCode){
        return getParentFor((BalanceAccount)aview.getRoot().getValue(), balAccCode) != null;
    }
    
    /**
     * The method returns parent account for th given code. It finds parent in tree
     * and starts finding from root.
     * @param root TreeRoot
     * @param code Founded account code.
     * @return 
     */
    private BalanceAccount getParentFor(BalanceAccount root, String code){
        BalanceAccount result = null;
        String accounCode = (root == null) ? "" : "" + root.getBalAcc(); // root account is null.
        ObservableList<BalanceAccount> children = (root == null) ? roots : root.childrenAccounts;
        if (isDirectChildNode(accounCode, code)) 
            result = root;
        for (BalanceAccount childAccount : children) {
            BalanceAccount newParent = getParentFor(childAccount, code);
            if (newParent != null){
                result = newParent;
                break;
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

    
    public class BalanceAccountsRunnable implements Runnable {
        
        private FilterModel model;
        
        public BalanceAccountsRunnable(FilterModel model) {
            this.model = model;
        }
        
        @Override
        public void run() {
            Platform.runLater(() -> {
                masker.setVisible(true); 
            });
            
            try {
                List<BalanceAccount> data = (model == null) ? dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL) : dataFetchProvider.getFilteredBy(model);
                data.stream().forEach((account) -> {
                    makeTreeStructure((BalanceAccount) account);
                });
                Platform.runLater(() -> {
                    roots.stream().forEach((account) -> {
                        aview.append(account);
                    });
                    aview.expand(1);
                });
            } catch (Exception ex) {
            } finally {
                Platform.runLater(() -> {
                    masker.setVisible(false);
                });
            }
        }
        
        
        private void makeTreeStructure(BalanceAccount account) {
            int accountCode = account.getBalAcc();
            if (accountCode % 1000 == 0) {
                roots.add(account);
            } else {
                BalanceAccount parentAccount = getParentFor((BalanceAccount)aview.getRoot().getValue(), "" + accountCode);
                if (parentAccount != null) {
                    parentAccount.childrenAccounts.add(account);
                }
            }
        }
    }
}
