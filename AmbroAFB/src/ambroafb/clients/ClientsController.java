/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.AmbroAFB;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private void addClient(ActionEvent e) {
        try{
            Stage stage = Utils.createStage("/ambroafb/clients/viewadd/AddClient.fxml", GeneralConfig.getInstance().getTitleFor("add_client"), "/images/innerLogo.png", AmbroAFB.mainStage);
            stage.setResizable(false);
            stage.show();
        } catch(IOException ex){ AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, Names.ERROR_MAIN_CONFIGURATION); alert.showAlert();}
        
        
        
    }
    
    @FXML 
    private void refresh(ActionEvent e) {
        table.getItems().clear();
        asignTable();
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
