/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.viewadd;

import ambroafb.clients.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class EditClientController implements Initializable {

    @FXML
	private Parent addClient;
    @FXML 
        private AddClientController addClientController;
    public Client client; 
        
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        
        addClientController.firstName.setText("ttttttttttttttttttt");// TODO
    }    
    
}
