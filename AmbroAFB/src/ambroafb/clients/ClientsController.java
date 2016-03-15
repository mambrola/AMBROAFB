/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.clients.editor_panel.EditorPanel;
import ambroafb.clients.viewadd.client_dialog.ClientDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;

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
//    @FXML
//    private Button edit, view;

    @FXML
    private void enter(ActionEvent e) {
        System.out.println("passed: Enter");
    }

    @FXML
    private void viewClient(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client);
        dialog.setDisabled();
        dialog.askClose(false);
        dialog.showAndWait();
    }

    @FXML
    private void editClient(ActionEvent e) {

        Client editingClient = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(editingClient);
        dialog.showAndWait();
        if (dialog.isCancelled()) {
            System.out.println("dialog is cancelled");
        } else {
            System.out.println("changed client: " + dialog.getResult());
        }
    }

    @FXML
    private void addClient(ActionEvent e) {
        ClientDialog dialog = new ClientDialog();
        dialog.showAndWait();

        if (dialog.isCancelled()){
            System.out.println("dialog is cancelled addClient");
        }else{
            System.out.println("changed client: "+dialog.getResult());
        }
    }
    
    @FXML 
    private void addBySample(ActionEvent e) {
        ClientDialog dialog = new ClientDialog();
        dialog.showAndWait();
        if (dialog.isCancelled()){
            System.out.println("dialog is cancelled addBySample");
        }else{
            System.out.println("changed client: "+dialog.getResult());

        }
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        //table.getItems().clear();
        ((Client) table.getItems().get(0)).setIsJur(!((Client) table.getItems().get(0)).getIsJur());//asignTable();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignTable();
//        edit.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
//        view.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    }

    private void asignTable() {
        Client.dbGetClients(0).values().stream().forEach((client) -> {
            table.getItems().add(client);
        });
//        if (table.getItems().size() > 0) {
//            table.getSelectionModel().select(0);
//        }
    }
}
