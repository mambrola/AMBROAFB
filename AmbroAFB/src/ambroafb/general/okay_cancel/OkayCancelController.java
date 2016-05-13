/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.clients.dialog.ClientDialogController;
import ambroafb.general.AlertMessage;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class OkayCancelController implements Initializable {

    @FXML
    private Button okay;
    @FXML
    private Button cancel;

    private boolean showConfirmationOnOkay;
    private boolean showConfirmationOnCancel;
    private boolean allowOperation;
    private String confirmationText ;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showConfirmationOnOkay = false;
        showConfirmationOnCancel = false;
        allowOperation = false;
        
        confirmationText = "Do you want to exit without #?";
        
        OkayButtonListener OkayListener = new OkayButtonListener();
        CancelButtonListener cancelListener = new CancelButtonListener();
        
        kayEventChange(okay.getParent());
        okay.setOnAction(OkayListener);
        cancel.setOnAction(cancelListener);
    }
    
    private void kayEventChange(Parent root){
        Utils.getFocusTraversableBottomChildren(root).stream().forEach((node) -> {
            node.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.SPACE)) {
                    event.consume();
                } 
                else if (event.getCode().equals(KeyCode.ENTER)) {
                    ((Button) node).fire();
                    event.consume();
                }
            });
        });
    }
    
    public void setConfirmationShowBy(EDITOR_BUTTON_TYPE type){
        switch (type) {
            case EDIT:
                showConfirmationOnOkay = false;
                showConfirmationOnCancel = true;
                break;
            case DELETE:
                showConfirmationOnOkay = true;
                showConfirmationOnCancel = false;
                break;
            case ADD:
                showConfirmationOnOkay = false;
                showConfirmationOnCancel = true;
                break;
            default:
                showConfirmationOnOkay = false;
                showConfirmationOnCancel = false;
                break;
        }
    }
    
    public void changeOkayButtonTitleFor(EDITOR_BUTTON_TYPE type){
        if ( !type.equals(EDITOR_BUTTON_TYPE.VIEW) ){
            String typeRealName = type.toString();
            String newTitle = typeRealName.charAt(0) + typeRealName.toLowerCase().substring(1);
            okay.setText(newTitle);
        }
        
        confirmationText = confirmationText.replace("#", okay.getText().toLowerCase());
    }
    
    public void makeButtonsVisibleBy(EDITOR_BUTTON_TYPE type){
        if (type.equals(EDITOR_BUTTON_TYPE.VIEW)){
            cancel.setVisible(false);
        }
        okay.setDisable(false);
        cancel.setDisable(false);
    }
    
    public boolean allowToMakeOperation(){
        return allowOperation;
    }
    
    private void onClose(){
        Stage currentStage = (Stage) cancel.getScene().getWindow();
        currentStage.close();
    }

    public void showAlertAndCheckClickForClose(){
        AlertMessage alert = new AlertMessage(Alert.AlertType.CONFIRMATION, null, confirmationText);
        ButtonType alertButtonType = alert.showAndWait().get();
        if (alertButtonType.equals(ButtonType.OK)){
            allowOperation = true;
            onClose();
        }
    }
    
    private class OkayButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (showConfirmationOnOkay){
                showAlertAndCheckClickForClose();
            }
            else {
                onClose();
            }
        }
        
    }
    
    private class CancelButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ClientDialogController controller = (ClientDialogController) cancel.getScene().getProperties().get("controller");
            if (showConfirmationOnCancel && controller.anyFieldWillChange()){ // && controller.anyChange()
                showAlertAndCheckClickForClose();
            }
            else {
                onClose();
            }
        }
    }
    
}
