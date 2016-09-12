/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambro.ATableView;
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
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrenciesController implements Initializable {

    @FXML
    private ATableView<EditorPanelable> aview;
    @FXML
    private MaskerPane masker;
    @FXML
    private EditorPanelController editorPanelController;
    
    private final ObservableList<EditorPanelable> currencies = FXCollections.observableArrayList();
    
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
        editorPanelController.setTableDataList(aview, currencies);
        editorPanelController.removeButtonsByFxIDs("#search");
        reAssignTable(null);
    }

     /**
     * The method call firstly in initialize and secondly, when user clicks refresh button.
     * @param jsonFilter The parameter is not need in it, 
     *                   but this is reAssignTable() method header agreement.
     */
    public void reAssignTable(JSONObject jsonFilter){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        currencies.clear();
        masker.setVisible(true);
        
        new Thread(() -> {
            currencies.setAll(Currency.getAllFromDBTest());
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }

    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
    
}
