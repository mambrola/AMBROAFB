/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.general.StagesContainer;
import ambroafb.general.interfaces.Filterable;
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
 * @author mambroladze
 */
public class FilterOkayCancelController implements Initializable {

    @FXML
    private Button okay, cancel;
    
//    private final BooleanProperty disableProperty = new SimpleBooleanProperty();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        setOkaydisable(true);
//        okay.disableProperty().bind(disableProperty);
    }    
    
    @FXML
    public void okay(ActionEvent event) {
        ((Filterable)okay.getScene().getProperties().get("controller")).setResult(true);
        ((Stage) okay.getScene().getWindow()).close();
    }
    @FXML
    public void cancel(ActionEvent event) {
        ((Filterable)okay.getScene().getProperties().get("controller")).setResult(false);
        ((Stage) okay.getScene().getWindow()).close();
        
        StagesContainer.removeByStage((Stage) okay.getScene().getWindow());
    }
    
//    public BooleanProperty disableProperty(){
//        return disableProperty;
//    }
    
    public void setOkaydisable(boolean disable){
        okay.setDisable(disable);
    }
}
