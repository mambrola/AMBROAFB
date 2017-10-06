/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.accounts.dialog.AccountDialog;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class DialogOkayCancelController implements Initializable {

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
                okay.setText(GeneralConfig.getInstance().getTitleFor("delete"));
                alertText = "You Realy want Delete this item?";
                okay.setOnAction((ActionEvent event) -> {
                    String stageName = ((Stage)okay.getScene().getWindow()).getTitle();
                    if(new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText, stageName).showAndWait().get().equals(ButtonType.OK)){
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
                okay.setText(GeneralConfig.getInstance().getTitleFor(type.equals(EDITOR_BUTTON_TYPE.ADD) ? "add" : "save"));
                okay.setOnAction((ActionEvent event) -> {
                    Scene currScene = okay.getScene();
                    Object controller = currScene.getProperties().get("controller");
                    boolean allRequiredFieldsAreValid = Utils.everyFieldContentIsValidFor(controller, type);
                    if (allRequiredFieldsAreValid){
                        changeClosePermissionForStage(true);
                        ((Stage) okay.getScene().getWindow()).close();
                    }
                });
                alertText = "Close without saving changes?";    
                cancel.setOnAction((ActionEvent event) -> {
                    boolean anyFieldWasChanged;

                    // ------------------- for test:
                    String stageTitle = ((Stage)cancel.getScene().getWindow()).getTitle();
                    if (stageTitle.equals(GeneralConfig.getInstance().getTitleFor("account_dialog_title"))){
                        anyFieldWasChanged = ((AccountDialog)cancel.getScene().getWindow()).anyComponentChanged();
                    }
                    else {
                        Object ownerObject = cancel.getScene().getProperties().get("controller");
                        anyFieldWasChanged = (Boolean) Utils.getInvokedClassMethod(ownerObject.getClass(), "anyComponentChanged", null, ownerObject);
                    }
                    // ------------------------- 
                    
                    if (anyFieldWasChanged) {
                        String stageName = ((Stage)okay.getScene().getWindow()).getTitle();
                        AlertMessage alert = new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText, stageName);
                        alert.setOwner((Stage)okay.getScene().getWindow());
                        ButtonType buttonType = alert.showAndWait().get();
                        if (buttonType.equals(ButtonType.OK)){
                            operationCanceled();
                            changeClosePermissionForStage(true);
                            ((Stage) okay.getScene().getWindow()).close();
                        }
                        else{
                            changeClosePermissionForStage(false);
                        }
                    }else{ // This case is needed. If nothing change the real object must become null.
                        operationCanceled();
                        changeClosePermissionForStage(true);
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
        ((Dialogable)cancel.getScene().getWindow()).operationCanceled();
//        Object controller = cancel.getScene().getProperties().get("controller");
//        Utils.getInvokedClassMethod(controller.getClass(), "operationCanceled", null, controller);
    }

    private void changeClosePermissionForStage(boolean value) {
        // ------------------- for test:
        String stageTitle = ((Stage)cancel.getScene().getWindow()).getTitle();
        if (stageTitle.equals(GeneralConfig.getInstance().getTitleFor("account_dialog_title"))){
            ((AccountDialog)cancel.getScene().getWindow()).changePermissionForClose(value);
        }
        else {
            Stage stage = ((Stage) okay.getScene().getWindow());
            Object controller = stage.getScene().getProperties().get("controller");
            Utils.getInvokedClassMethod(controller.getClass(), "changePermissionForClose", new Class[]{boolean.class}, controller, value);
        }
        // ------------------- 
    }
    
}
