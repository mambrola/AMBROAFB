/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.clients.dialog.ClientDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> table;
    
    @FXML
    private EditorPanel panel;

    @FXML
    private void delete(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client);
        dialog.setDisabled();
        dialog.askClose(false);
        
        ((Button)dialog.getScene().getRoot().lookup("#okay")).setText("Delete");
        dialog.getScene().getRoot().lookup("#okay").setDisable(false);
        dialog.getScene().getRoot().lookup("#cancel").setDisable(false);
        
        dialog.showAndWait();
        
    }

    @FXML
    private void edit(ActionEvent e) {

        Client editingClient = table.getSelectionModel().getSelectedItem();
        Client backup = editingClient.cloneWithID();
        
        ClientDialog dialog = new ClientDialog(editingClient);
        Client editedClient = dialog.getResult();
        if (editedClient == null) {
            editingClient.copyFrom(backup);
            System.out.println("dialog is cancelled");
        } else {
            System.out.println("changed client: " + editedClient);
            System.out.println("phones = "+editedClient.getPhoneNumbers());
        }
    }

    @FXML
    private void view(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client);
        dialog.setDisabled();
        dialog.askClose(false);
        dialog.showAndWait();
    }

    @FXML
    private void add(ActionEvent e) {
        ClientDialog dialog = new ClientDialog();
        Client newClient = dialog.getResult();

        if (newClient == null){
            System.out.println("dialog is cancelled addClient");
        }else{
            System.out.println("changed client: "+newClient);
        }
    }
    
    @FXML 
    private void addBySample(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client.cloneWithoutID());
        Client newClient = dialog.getResult();
        if (newClient == null){
            System.out.println("dialog is cancelled addBySample");
        }else{
            System.out.println("changed client: "+newClient);

        }
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        
        table.getItems().get(0).setEmail("ccccccc");

        //table.getItems().clear();
        //asignTable();
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignTable();
    }

    private void asignTable() {
        Client.getClients().stream().forEach((client) -> {
            table.getItems().add(client);
        });
        panel.disablePropertyBinder(table);
    }
}
