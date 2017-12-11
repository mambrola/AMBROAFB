/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts.dialog;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentTreeItem;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
public class BalanceAccountDialogController extends DialogController {

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
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
    }
    
    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
         if (object != null){
            BalanceAccount balanceAccount = (BalanceAccount)object;
            balAccountCode.textProperty().bindBidirectional(balanceAccount.balAccProperty());
            balAccountName.textProperty().bindBidirectional(balanceAccount.descripProperty());
            actPassChecker.selectedProperty().bindBidirectional(balanceAccount.actPasProperty());
            actPassChecker.indeterminateProperty().bindBidirectional(balanceAccount.indeterminateProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.EDIT) && !((BalanceAccount)sceneObj).childrenAccounts.isEmpty()){
            balAccountCode.setDisable(true);
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    
    public Object getOwnerController(){
        Window ownerStage = ((Stage)balAccountCode.getScene().getWindow()).getOwner();
        return ownerStage.getScene().getProperties().get("controller");
    }
    
    public EditorPanelable getNewEditorPanelable(){
        return sceneObj;
    }

    @Override
    protected void removeBinds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void removeListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
