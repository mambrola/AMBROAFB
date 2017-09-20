/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion.dialog;

import ambro.ADatePicker;
import ambroafb.accounts.AccountComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.docs.types.conversion.Conversion;
import ambroafb.general.Names;
import ambroafb.general.Utils;
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
public class ConversionDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private IsoComboBox currencyFromAccount;
    @FXML @ContentNotEmpty
    private AccountComboBox fromAccount;
    @FXML @ContentNotEmpty
    private TextField amountFromAccount;
    
    @FXML
    private IsoComboBox currencyToAccount;
    @FXML @ContentNotEmpty
    private AccountComboBox toAccount;
    @FXML @ContentNotEmpty
    private TextField amountToAccount;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    private Conversion conversion, conversionBackup;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
        
        fromAccount.fillComboBox();
        toAccount.fillComboBox();
        
        currencyFromAccount.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            fromAccount.filterBy(newValue);
            currencyToAccount.filterBy(newValue);
        });
        currencyToAccount.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            toAccount.filterBy(newValue);
        });
    }    

    public void bindObject(Conversion conversion) {
        this.conversion = conversion;
        if(conversion != null){
            docDate.valueProperty().bindBidirectional(conversion.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(conversion.docInDocDateProperty());
            currencyFromAccount.valueProperty().bindBidirectional(conversion.currencyFromAccountProperty());
            currencyToAccount.valueProperty().bindBidirectional(conversion.currencyToAccountProperty());
            fromAccount.valueProperty().bindBidirectional(conversion.accountFromProperty());
            toAccount.valueProperty().bindBidirectional(conversion.accountToProperty());
            amountFromAccount.textProperty().bindBidirectional(conversion.amountFromAccountProperty());
            amountToAccount.textProperty().bindBidirectional(conversion.amountToAccountProperty());
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
    
    public void setBackupDoc(Conversion conversionBackup) {
        this.conversionBackup = conversionBackup;
    }
    
    public boolean anyComponentChanged(){
        return !conversion.compares(conversionBackup);
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
