/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.currency_rates.CurrencyRatesComboBox;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.mapeditor.MapEditorComboBox;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.products.Product;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ProductDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML @ContentNotEmpty
    private TextField abbreviation, descrip, remark, price;
    @FXML @ContentNotEmpty // @ContentPattern
    private TextField former;
    @FXML
    private ComboBox<String> specifics;
    @FXML
    private CurrencyRatesComboBox currency;
    @FXML
    private MapEditorComboBox discounts;
    @FXML
    private CheckBox isAlive;
    
    
//    @FXML
//    @ContentNotEmpty
//    private TextField  productNameField, vendorCodeField, productRemarkField;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Product product, productBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        new Thread(() -> {
            Product.getAllSpecifics().forEach((specific) -> {
                specifics.getItems().add(specific.getValue());
            });
        }).start();
        permissionToClose = true;
    }

    public void bindProduct(Product product) {
        this.product = product;
        if (product != null){
            abbreviation.textProperty().bindBidirectional(product.abbreviationProperty());
            former.textProperty().bindBidirectional(product.formerProperty());
            descrip.textProperty().bindBidirectional(product.descriptionProperty());
            remark.textProperty().bindBidirectional(product.remarkProperty());
            specifics.valueProperty().bindBidirectional(product.specificProperty());
            price.textProperty().bindBidirectional(product.priceProperty());
            currency.valueProperty().bindBidirectional(product.currencyProperty());
//            discounts.setItems(product.getDiscounts());
            isAlive.selectedProperty().bindBidirectional(product.isAliveProperty());
        }
    }
    
    public boolean anyComponentChanged(){
        return !product.compares(productBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType, String string) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }

    public void setBackupProduct(Product productBackup) {
        this.productBackup = productBackup;
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
