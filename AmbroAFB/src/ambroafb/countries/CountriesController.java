/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

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
 * @author murman
 */
public class CountriesController implements Initializable {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    @FXML
    private MaskerPane masker;

    @FXML
    private EditorPanelController editorPanelController;
    
    private final ObservableList<EditorPanelable> countries = FXCollections.observableArrayList();;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, countries);
        editorPanelController.removeButtonsByFxIDs("#delete", "#edit", "#view", "#add", "#refresh");
        reAssignTable(null);
    }

    private void reAssignTable(FilterModel model) {
        final int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        countries.clear();
        masker.setVisible(true);
        new Thread(() -> {
            Country.getAllFromDB().stream().forEach((country) -> {
                countries.add(country);
            });
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }
    
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }

}
