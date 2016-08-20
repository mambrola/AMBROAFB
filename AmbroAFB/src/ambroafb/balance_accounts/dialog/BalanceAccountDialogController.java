/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
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
public class BalanceAccountDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
//    @Annotations.ContentNotEmpty
    private TextField  balAccountName;
    
    @FXML
//    @Annotations.ContentNotEmpty
//    @Annotations.ContentPattern(value="([0,9]{,4})", explain="The length must be four and content only digits.")
    private TextField  balAccountCode;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private BalanceAccount balAccount, balAccountBackup;
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

    public void bindBalAccount(BalanceAccount balAccount) {
        this.balAccount = balAccount;
        if (balAccount != null){
            balAccountCode.textProperty().bindBidirectional(balAccount.codeProperty());
            balAccountName.textProperty().bindBidirectional(balAccount.currDescripProperty());
        }
    }
    
    public boolean anyComponentChanged(){
        return !balAccount.compares(balAccountBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType, String string) {
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

    public void setBackupBalAccount(BalanceAccount balAccountBackup) {
        this.balAccountBackup = balAccountBackup;
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
}
