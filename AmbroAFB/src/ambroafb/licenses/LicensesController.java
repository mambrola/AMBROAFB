/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

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
 * @author Dato
 */
public class LicensesController implements Initializable {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    @FXML
    private EditorPanelController editorPanelController;
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> licenses = FXCollections.observableArrayList();
    
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
        editorPanelController.setTableDataList(aview, licenses);
        editorPanelController.removeButtonsByFxIDs("#delete", "#edit", "#view", "#add");
    }    
    
    public void reAssignTable(FilterModel model){
        if (!model.isEmpty()) {
            int selectedIndex = aview.getSelectionModel().getSelectedIndex();
            licenses.clear();
            masker.setVisible(true);
            Thread t = new Thread(() -> {
                License.getFilteredFromDB(model).stream().forEach((license) -> {
                    licenses.add(license);
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
    }
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
}
