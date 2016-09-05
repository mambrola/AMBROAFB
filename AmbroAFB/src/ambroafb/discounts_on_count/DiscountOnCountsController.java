/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

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
public class DiscountOnCountsController implements Initializable {

    @FXML
    private ATableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    private final ObservableList<EditorPanelable> discounts = FXCollections.observableArrayList();
    
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
        editorPanelController.setTableDataList(aview, discounts);
        editorPanelController.removeButtonsByFxIDs("#search");
        reAssignTable(null);
    }
    
    public void reAssignTable(JSONObject filterJson) {
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        discounts.clear();
        Platform.runLater(() -> {
            masker.setVisible(true);
        });
        Thread t = new Thread(() -> {
            DiscountOnCount.getAllFromDBTest().stream().forEach((client) -> {
                discounts.add(client);
            });

            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        });
        t.start();
    }

    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
    
}
