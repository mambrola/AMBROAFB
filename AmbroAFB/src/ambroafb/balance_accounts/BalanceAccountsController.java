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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private EditorPanelController editorPanelController;
            
    
    private final ObservableList<EditorPanelable> accounts = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(treeTable);
//        editorPanelController.setTableDataList(table, accounts);
        editorPanelController.removeButtonsByFxIDs("#delete", "#edit", "#view", "#add", "#refresh");
        reAssignTable(null);
    }

    public void reAssignTable(JSONObject filterJson){
        accounts.clear();
        new Thread(() -> {
            BalanceAccount.getAllFromDB().stream().forEach((account) -> {
                accounts.add(account);
            });
        }).start();
    }

    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
}
