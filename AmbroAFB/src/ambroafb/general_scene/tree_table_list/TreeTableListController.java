/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.tree_table_list;

import ambro.AFilterableTreeTableView;
import ambroafb.balance_accounts.*;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TreeTableListController extends ListingController {

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
    public void reAssignTable(FilterModel model){
        roots.clear();
        aview.removeAll();
//        new Thread(new BalanceAccountsController.BalanceAccountsRunnable(model)).start();
    }
    
    @Override
    public void addListWith(Class content) {
        aview.initialize(content);
        editorPanel.buttonsMainPropertiesBinder(aview);
        editorPanel.setTreeTable(aview);
    }

}
