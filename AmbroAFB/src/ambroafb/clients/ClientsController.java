/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.clients.dialog.ClientDialog;
import ambroafb.general.editor_panel.EditorPanelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> table;

    @FXML
    private EditorPanelController editorPanelController;
    

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(table);
        asignTable();
    }

    //შეიძლება გატანა მშობელ კლასში
    public void asignTable() {
        Client.getClients().stream().forEach((client) -> {
            table.getItems().add(client);
        });
    }
    
    //შეიძლება გატანა მშობელ კლასში refresh-თან EditorPanelController-ში
    public void selectOneAgain(Client selected) {
        int i = table.getItems().size() - 1;
        while(i >= 0 && table.getItems().get(i).getRecId() != selected.getRecId())
            i--;
        if(i >= 0)
            table.getSelectionModel().select(i);
    }
}
