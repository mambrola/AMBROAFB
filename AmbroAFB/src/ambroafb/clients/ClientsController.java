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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> clients = FXCollections.observableArrayList();
    private boolean allowToClose;
    private BooleanProperty closePermission;
    
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
        closePermission = new SimpleBooleanProperty();
        allowToClose = true;
    }

    public void reAssignTable(JSONObject filterJson) {
        if (filterJson != null && filterJson.length() > 0) {
            clients.clear();
            Platform.runLater(() -> {
                masker.setVisible(true);
            });
            Thread t = new Thread(() -> {
                Client.getFilteredFromDB(filterJson).stream().forEach((client) -> {
                    clients.add(client);
                });
                
                Platform.runLater(() -> {
                    masker.setVisible(false);
                });
            });
            t.start();
        }
    }

    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
    
    public void changePermissionForClose(boolean value){
        allowToClose = value;
        System.out.println("ClientsController. Alert Cancel click... allowToClose: " + allowToClose);
    }
    
    public boolean allowToClose(){
        return allowToClose;
    }
}
