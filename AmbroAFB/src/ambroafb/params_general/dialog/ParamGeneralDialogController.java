/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.dialog;

import ambroafb.clients.ClientComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.minitables.attitudes.AttitudeComboBox;
import ambroafb.minitables.merchandises.MerchandiseComboBox;
import ambroafb.params_general.ParamGeneral;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ParamGeneralDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML
    private ClientComboBox clients;
//    private TextField client;
    @FXML 
    private AttitudeComboBox attitudes;
    @FXML 
    private MerchandiseComboBox merchandises;
    @FXML 
    private TextField paramType;
    @FXML 
    private TextField param;
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private ParamGeneral paramGeneral, paramGeneralBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
    }    


    public boolean anyComponentChanged(){
        return !paramGeneral.compares(paramGeneralBackup);
    }

    void bindParamGeneral(ParamGeneral paramGeneral) {
        this.paramGeneral = paramGeneral;
        if (paramGeneral != null){
            clients.valueProperty().bindBidirectional(paramGeneral.clientProperty());
            attitudes.valueProperty().bindBidirectional(paramGeneral.attitudeProperty());
            merchandises.valueProperty().bindBidirectional(paramGeneral.merchandiseProperty());
            paramType.textProperty().bindBidirectional(paramGeneral.paramTypeProperty());
            param.textProperty().bindBidirectional(paramGeneral.paramProperty());
        }
    }

    void setBackupParamGeneral(ParamGeneral paramGeneralBackup) {
        this.paramGeneralBackup = paramGeneralBackup;
    }
    
    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    private void setDisableComponents() {
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }  
    
}
