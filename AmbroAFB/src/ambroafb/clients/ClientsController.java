/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

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
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private ATableView<EditorPanelable> table;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    private final ObservableList<EditorPanelable> clients = FXCollections.observableArrayList();

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        table.setBundle(rb);
        editorPanelController.buttonsMainPropertysBinder(table);
        editorPanelController.setTableDataList(table, clients);
    }

    public void reAssignTable(JSONObject filterJson) {
        if (filterJson != null && filterJson.length() > 0) {
            clients.clear();
            Thread t = new Thread(() -> {
                Client.getFilteredFromDB(filterJson).stream().forEach((client) -> {
                    clients.add(client);
                });
            });
            t.start();
        }
    }

    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
}
