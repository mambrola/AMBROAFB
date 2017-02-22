/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells.dialog;

import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.minitables.buysells.BuySell;
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
public class BuySellDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML
    private TextField descrip;
    
    private BuySell buysell;
    private BuySell buysellBackup;
    
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
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

    public void bindBuySell(BuySell buysell) {
        this.buysell = buysell;
        if (buysell != null){
            descrip.textProperty().bindBidirectional(buysell.descripProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    private void setDisableComponents() {
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    public void setBackupBuySell(BuySell buysellBackup) {
        this.buysellBackup = buysellBackup;
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
        return !buysell.compares(buysellBackup);
    }
}
