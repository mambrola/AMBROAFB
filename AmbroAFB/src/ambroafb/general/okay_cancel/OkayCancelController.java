/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.general.AlertMessage;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class OkayCancelController implements Initializable {

    @FXML
    private Button okay, cancel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public Button getOkayButton(){
        return okay;
    }
    
    public Button getCancelButton(){
        return cancel;
    }

    public void setButtonsFeatures(EDITOR_BUTTON_TYPE type){
        String alertText;
        okay.setDisable(false);
        cancel.setDisable(false);
        switch (type) {
            case DELETE:
                okay.setText("Delete");
                alertText = "You Realy want Delete this item?";
                okay.setOnAction((ActionEvent event) -> {
                    if(new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText).showAndWait().get().equals(ButtonType.OK)){
                        ((Stage) okay.getScene().getWindow()).close();
                    }
                });
                cancel.setOnAction((ActionEvent event) -> {
                    operationCanceled();
                    ((Stage) okay.getScene().getWindow()).close();
                });
                break;
            case EDIT:
            case ADD:
                okay.setText(type.equals(EDITOR_BUTTON_TYPE.ADD) ? "Add" : "Save");
                okay.setOnAction((ActionEvent event) -> {
                    ((Stage) okay.getScene().getWindow()).close();
                });
                alertText = "Close without saving changes?";    
                cancel.setOnAction((ActionEvent event) -> {
                    boolean anyFieldWasChanged = false;
                    try {
                        anyFieldWasChanged = (boolean)cancel.getScene().getProperties().get("controller").getClass().getMethod("anyFieldChanged").invoke(cancel.getScene().getProperties().get("controller"));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) { Logger.getLogger(OkayCancelController.class.getName()).log(Level.SEVERE, null, ex); }
                    if(!anyFieldWasChanged || new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText).showAndWait().get().equals(ButtonType.OK)){
                        operationCanceled();
                        ((Stage) okay.getScene().getWindow()).close();
                    }
                });
                break;
            case VIEW:
                okay.setOnAction((ActionEvent event) -> {
                    operationCanceled();
                    ((Stage) okay.getScene().getWindow()).close();
                });
                cancel.setOnAction((ActionEvent event) -> {
                    operationCanceled();
                    ((Stage) okay.getScene().getWindow()).close();
                });
                cancel.setVisible(false);
        }
    }
    
    private void operationCanceled(){
        try {
            cancel.getScene().getProperties().get("controller").getClass().getMethod("operationCanceled").invoke(cancel.getScene().getProperties().get("controller"));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) { Logger.getLogger(OkayCancelController.class.getName()).log(Level.SEVERE, null, ex); }
    }                        
}
