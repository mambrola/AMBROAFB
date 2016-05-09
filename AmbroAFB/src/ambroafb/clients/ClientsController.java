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
    private BorderPane formPane;

    @FXML
    private TableView<Client> table;

    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private void delete(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client);
        dialog.setDisabled();
        dialog.askClose(false);

        ((Button) dialog.getScene().getRoot().lookup("#okay")).setText("Delete");
        dialog.getScene().getRoot().lookup("#okay").setDisable(false);
        dialog.getScene().getRoot().lookup("#cancel").setDisable(false);

        dialog.showAndWait();
        if (Client.deleteClient(client.recId)) {
            table.getItems().remove(client);
        }
    }


//    @FXML
//    private void edit(ActionEvent e) {
//
//        Client editingClient = table.getSelectionModel().getSelectedItem();
//        Client real = Client.getOneFromDB(editingClient.recId);
//        if (real != null) {
//            editingClient.copyFrom(real);
//        }
//        Client backup = editingClient.cloneWithID();
//
//        ClientDialog dialog = new ClientDialog(editingClient);
//        Client editedClient = dialog.getResult();
//        if (editedClient == null) {
//            editingClient.copyFrom(backup);
//            System.out.println("dialog is cancelled");
//        } else {
//            System.out.println("changed client: " + editedClient);
//            System.out.println("phones = " + editedClient.getPhoneNumbers());
//        }
//    }

//    @FXML
//    private void view(ActionEvent e) {
//        Client client = table.getSelectionModel().getSelectedItem();
//        Client real = Client.getOneFromDB(client.recId);
//        if (real != null) {
//            client.copyFrom(real);
//        }
//
//        ClientDialog dialog = new ClientDialog(client);
//        dialog.setDisabled();
//        dialog.askClose(false);
//        dialog.showAndWait();
//    }

//    @FXML
//    private void add(ActionEvent e) {
//        ClientDialog dialog = new ClientDialog();
//        Client newClient = dialog.getResult();
//
//        if (newClient == null) {
//            System.out.println("dialog is cancelled addClient");
//        } else {
//            System.out.println("changed client: " + newClient);
//            newClient = Client.saveClient(newClient);
//            if (newClient != null) {
//                table.getItems().add(newClient);
//            }
//        }
//    }
>>>>>>> origin/master

    @FXML
    private void addBySample(ActionEvent e) {
        Client client = table.getSelectionModel().getSelectedItem();
        ClientDialog dialog = new ClientDialog(client.cloneWithoutID());
        Client newClient = dialog.getResult();
        if (newClient == null) {
            System.out.println("dialog is cancelled addBySample");
        } else {
            System.out.println("changed client: " + newClient);
            Client.saveOneToDB(newClient);
        }
    }

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
