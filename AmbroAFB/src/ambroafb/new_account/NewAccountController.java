/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.new_account;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class NewAccountController implements Initializable {

    @FXML
    private DatePicker openDate;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openDate.setValue(LocalDate.now());
        // TODO
    }    
    
}
