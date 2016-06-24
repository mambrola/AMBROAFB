/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.exit_button;

import ambroafb.general.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class ExitButtonController implements Initializable {
    
    @FXML
    private Button exitButton;
    
    @FXML
    private void exit(ActionEvent e) {
        Stage stage = (Stage) (exitButton.getScene().getWindow());
        stage.close();
        Utils.removeByStage(stage);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
