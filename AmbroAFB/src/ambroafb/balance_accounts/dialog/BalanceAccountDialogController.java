/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentTreeItem;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class BalanceAccountDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    @ContentNotEmpty
    @ContentTreeItem(valueForLength = "4", valueForSyntax = "[0-9]+")
    private TextField  balAccountCode;
    
    @FXML
    @ContentNotEmpty
    private TextField  balAccountName;
    
    @FXML
    private CheckBox actPassChecker;
    
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
//        Utils.validateTextFieldContentListener(balAccountCode, "[1-9][0-9]*");
        permissionToClose = true;
    }    

    public void bindBalAccount(BalanceAccount balAccount) {
        this.balAccount = balAccount;
        if (balAccount != null){
            balAccountCode.textProperty().bindBidirectional(balAccount.balAccProperty());
            balAccountName.textProperty().bindBidirectional(balAccount.descripProperty());
            actPassChecker.selectedProperty().bindBidirectional(balAccount.actPasProperty());
            actPassChecker.indeterminateProperty().bindBidirectional(balAccount.indeterminateProperty());
        }
    }
    
    public boolean anyComponentChanged(){
        return !balAccount.compares(balAccountBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.EDIT) && !balAccount.childrenAccounts.isEmpty()){
            balAccountCode.setDisable(true);
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
    
    public Object getOwnerController(){
        Window ownerStage = ((Stage)balAccountCode.getScene().getWindow()).getOwner();
        return ownerStage.getScene().getProperties().get("controller");
    }
    
    public EditorPanelable getNewEditorPanelable(){
        return balAccount;
    }
}
