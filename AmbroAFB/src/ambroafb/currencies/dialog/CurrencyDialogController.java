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
    @FXML @ContentNotEmpty @ContentPattern(value = "\\p{Currency_Symbol}", explain = "Only one symbol of current.")
    private TextField symbol;
    @FXML
    private DialogOkayCancelController okeyCancelController;
    
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
        Utils.validateTextFieldContent(symbol, "\\p{Currency_Symbol}");
        currencies.setShowCategoryALL(false);
        permissionToClose = true;
    }    


    public void bindCurrency(Currency currency) {
        this.currency = currency;
        if (currency != null){
//            currencies.valueProperty().bindBidirectional(currency.isoProperty());
            descrip.textProperty().bindBidirectional(currency.descripProperty());
            symbol.textProperty().bindBidirectional(currency.symbolProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setBackupCurrency(Currency currencyBackup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okeyCancelController;
    }
}
