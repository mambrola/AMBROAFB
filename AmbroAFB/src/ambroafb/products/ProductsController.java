/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AFilterableTableView;
import ambroafb.general.FilterModel;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ProductsController implements Initializable {
    
    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> products = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, products);
        reAssignTable(null);
    }
    
    /**
     * The method call firstly in initialize and secondly, when user clicks refresh button.
     * @param model The parameter is not need in it, 
     *                   but this is reAssignTable() method header agreement.
     */
    public void reAssignTable(FilterModel model) {
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        products.clear();
        masker.setVisible(true);

        new Thread(() -> {
            products.setAll(Product.getAllFromDB());
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0) {
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }
    
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
}
