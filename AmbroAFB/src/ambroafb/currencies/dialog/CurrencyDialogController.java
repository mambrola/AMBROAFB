/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Dialogable;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML @ContentNotEmpty
    private CurrencyComboBox currencies;
    @FXML @ContentNotEmpty
    private TextField descrip;
//    @FXML @ContentNotEmpty @ContentPattern(value = "\\p{Currency_Symbol}", explain = "Only one symbol of current.")
//    @FXML @ContentNotEmpty @ContentPattern(value = "\\p", explain = "Only one symbol of current.")
    @FXML
    private TextField symbol;
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Currency currency, currencyBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
//        Utils.validateTextFieldContent(symbol, "\\p");
        currencies.setShowCategoryALL(false);
        permissionToClose = true;
    }    


    public void bindCurrency(Currency currency) {
        this.currency = currency;
        if (currency != null){
            currencies.valueProperty().bindBidirectional(currency.currencyProperty());
            descrip.textProperty().bindBidirectional(currency.descripProperty());
            symbol.textProperty().bindBidirectional(currency.symbolProperty());
        }
    }

    public boolean anyComponentChanged(){
        return !currency.compares(currencyBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    public void setBackupCurrency(Currency currencyBackup) {
        this.currencyBackup = currencyBackup;
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
