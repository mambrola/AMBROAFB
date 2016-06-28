/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.clients.Client;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author murman
 */
public class CountriesController implements Initializable {

    @FXML
    private TableView<EditorPanelable> table;

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
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(table);
        editorPanelController.setTableDataList(table, countries);
        reAssignTable(null);
        editorPanelController.showButtonsByOrderOf(true, false, false, false, false, false, true);
    }

    private void reAssignTable(JSONObject json) {
        countries.clear();
        Thread t = new Thread(() -> {
            Country.getAllFromDB().stream().forEach((country) -> {
                countries.add(country);
            });
        });
        t.start();
    }
    
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }

}
