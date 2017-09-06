/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambro.ADatePicker;
import ambroafb.docs.Doc;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class CustomDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private ComboBox<String> debits, credits; //  Must be specific comboBoxes  -------------- 
    
    @FXML
    private TextField amount, currency, docCode, descrip;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    private Doc doc, docBackup;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
        
        Utils.validateTextFieldContentListener(amount, "\\d+|\\d+\\.|\\d+\\.\\d*");
    }    

    

    public void bindDoc(Doc doc) {
        this.doc = doc;
        if (doc != null){
            docDate.valueProperty().bindBidirectional(doc.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(doc.docInDocDateProperty());
//            debits
//            credits
            amount.textProperty().bindBidirectional(doc.amountProperty());
            currency.textProperty().bindBidirectional(doc.isoProperty());
            docCode.textProperty().bindBidirectional(doc.docCodeProperty());
            descrip.textProperty().bindBidirectional(doc.descripProperty());
            
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    public void setBackupDoc(Doc docBackup) {
        this.docBackup = docBackup;
    }
    
    public boolean anyComponentChanged(){
        return !doc.compares(docBackup);
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
