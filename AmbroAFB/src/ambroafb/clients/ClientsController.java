/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.AmbroAFB;
import ambroafb.clients.viewadd.client_dialog.ClientDialog;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> table;
    
    @FXML private void enter(ActionEvent e) {System.out.println("passed: Enter");  }
    
    @FXML 
    private void viewClient(ActionEvent e) {
        try{
            Stage stage = Utils.createStage("/ambroafb/clients/viewadd/ViewClient.fxml", GeneralConfig.getInstance().getTitleFor("view_client"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){ AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION); alert.showAlert();}
    }
    
    @FXML 
    private void editClient(ActionEvent e) {
        
        System.out.println("selected: " + table.getSelectionModel().getSelectedItem().clientId);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("client", table.getSelectionModel().getSelectedItem());
        try{
            Stage stage = Utils.createStage("/ambroafb/clients/viewadd/EditClient.fxml", parameters, GeneralConfig.getInstance().getTitleFor("edit_client"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
            System.out.println("stage.getScene().lookup: " + stage.getScene().lookup("client"));
        } catch(IOException ex){ AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION); alert.showAlert();}
    }
        
    @FXML 
    private void addClient(ActionEvent e) {
        ClientDialog dialog = new ClientDialog();
        dialog.showAndWait();
        if (dialog.isCancelled()){
            System.out.println("dialog is cancelled");
        }else{
            System.out.println("created/changed client: "+dialog.getResult());
        }
//        try{
//            Stage stage = Utils.createStage("/ambroafb/clients/viewadd/AddClient.fxml", GeneralConfig.getInstance().getTitleFor("add_client"), "/images/innerLogo.png", AmbroAFB.mainStage);
//            stage.setResizable(false);
//            stage.show();
//        } catch(IOException ex){ AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION); alert.showAlert();}
    }
    
    @FXML 
    private void refresh(ActionEvent e) {
        //table.getItems().clear();
        ((Client)table.getItems().get(0)).isJur.set(!((Client)table.getItems().get(0)).isJur.getValue());//asignTable();
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignTable();
    }
    
    private void asignTable(){
        Client.dbGetClients(0).values().stream().forEach((client) -> {
            table.getItems().add(client);
        });
        
    }
}
