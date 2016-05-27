/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.clients.filter.ClientFilter;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<EditorPanelable> table;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    private ObservableList<EditorPanelable> clients = FXCollections.observableArrayList();;
    private SortedList<EditorPanelable> sorterData;
    private Stage stage;
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(table);
        editorPanelController.setTableDataList(table, clients);
    }

    public void reAssignTable(boolean isFirstTime) {
        JSONObject filterJson = new ClientFilter(stage).getResult();
        if(filterJson == null){
            if(isFirstTime)
                stage.close();
            else
                return; 
        }
        clients.clear();
        Client.getAllFromDB().stream().forEach((client) -> {
            clients.add(client);
        });
    }

    void informAboutStage(Stage stage) {
        this.stage = stage;
    }
}
