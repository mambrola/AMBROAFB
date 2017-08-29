/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.transfer.dialog;

import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class TransferUtilityDialogController implements Initializable {

    @FXML
    private DialogOkayCancelController okayCancelController;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
}
