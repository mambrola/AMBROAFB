/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.dialog;

import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.minitables.MiniTable;
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
public class MiniTableDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML
    private TextField rec_id;
    @FXML
    private TextField descrip;
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private MiniTable minitable, minitableBackup;
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
    
    public void bindSceneContentTo(MiniTable miniTable){
        this.minitable = miniTable;
        if (miniTable != null){
            rec_id.textProperty().bindBidirectional(miniTable.recIdProperty());
            descrip.textProperty().bindBidirectional(miniTable.descripProperty());
        }
    }
    
    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType){
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.EDIT)){
            rec_id.setDisable(true);
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void setDisableComponents() {
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void setBackup(MiniTable backup){
        minitableBackup = backup;
    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
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
    
    public boolean anyComponentChanged(){
        return !minitable.compares(minitableBackup);
    }
}
