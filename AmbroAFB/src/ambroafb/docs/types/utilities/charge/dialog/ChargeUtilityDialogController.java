/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge.dialog;

import ambro.ADatePicker;
import ambroafb.docs.DocMerchandise;
import ambroafb.docs.DocMerchandiseComboBox;
import ambroafb.docs.types.utilities.charge.ChargeUtility;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class ChargeUtilityDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private DocMerchandiseComboBox utilities;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private TextField currency;
    
    @FXML @Annotations.ContentNotEmpty @Annotations.ContentPattern(value = "\\d+(\\.\\d+)?", explain = "Amount Pattern is incorrct.")
    private TextField amount, vat;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private ChargeUtility chargeUtility, chargeUtilityBackup;
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
        
        // Value change of utility also changes iso:
        utilities.valueProperty().addListener((ObservableValue<? extends DocMerchandise> observable, DocMerchandise oldValue, DocMerchandise newValue) -> {
            currency.setText(newValue.getIso());
        });
        
        Utils.validateTextFieldContentListener(amount, "\\d+|\\d+\\.|\\d+\\.\\d*");
        Utils.validateTextFieldContentListener(vat, "\\d+|\\d+\\.|\\d+\\.\\d*");
    }    

    public void bindUtility(ChargeUtility chargeUtility) {
        this.chargeUtility = chargeUtility;
        if (chargeUtility != null){
            utilities.valueProperty().bindBidirectional(chargeUtility.merchandiseProperty());
            docDate.valueProperty().bindBidirectional(chargeUtility.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(chargeUtility.docInDocDateProperty());
            currency.textProperty().bindBidirectional(chargeUtility.isoProperty());
            amount.textProperty().bindBidirectional(chargeUtility.amountProperty());
            vat.textProperty().bindBidirectional(chargeUtility.vatProperty());
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

    public void setBackupCharge(ChargeUtility chargeUtilityBackup) {
        this.chargeUtilityBackup = chargeUtilityBackup;
    }
    
    
    public boolean anyComponentChanged(){
        return !chargeUtility.compares(chargeUtilityBackup);
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
