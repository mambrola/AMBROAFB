/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment.dialog;

import ambro.ADatePicker;
import ambroafb.docs.DocMerchandise;
import ambroafb.docs.DocMerchandiseComboBox;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
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
public class PaymentUtilityDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private DocMerchandiseComboBox utilities;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    private final String docDatePattern = "dd-MM-yyyy HH:mm:ss";
    
    @FXML
    private TextField currency;
    
    @FXML @ContentNotEmpty @ContentPattern(value = "\\d+(\\.\\d+)?", explain = "Amount Pattern is incorrct.")
    private TextField amount;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    
    private ArrayList<Node> focusTraversableNodes;
    private PaymentUtility paymentUtility, paymentUtilityBackup;
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
    }    

    public void bindUtility(PaymentUtility paymentUtility) {
        this.paymentUtility = paymentUtility;
        if (paymentUtility != null){
            utilities.valueProperty().bindBidirectional(paymentUtility.utilityProperty());
            docDate.valueProperty().bindBidirectional(paymentUtility.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(paymentUtility.docInDocDateProperty());
            currency.textProperty().bindBidirectional(paymentUtility.isoProperty());
            amount.textProperty().bindBidirectional(paymentUtility.amountProperty()); // -1 default value not show in dialog, becouse of "amount" textfield value pattern.
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

    public void setBackupPayment(PaymentUtility paymentUtilityBackup) {
        this.paymentUtilityBackup = paymentUtilityBackup;
    }
    
    public boolean anyComponentChanged(){
        return !paymentUtility.compares(paymentUtilityBackup);
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
