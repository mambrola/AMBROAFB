/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambro.ADatePicker;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.products.Product;
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
 * @author dato
 */
public class ProductDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML
    private ADatePicker openDate;
    
    @FXML
    @ContentNotEmpty
    private TextField productNameField, productRemarkField;
    
    @FXML
    private ComboBox<Product> productParents;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private Product product, productBackup;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        new Thread(() -> {
            productParents.getItems().addAll(Product.getAllFromDB());
        }).start();
    }    

    public void bindProduct(Product product) {
        this.product = product;
        if (product != null){
            productNameField.textProperty().bindBidirectional(product.descriptionProperty());
            productRemarkField.textProperty().bindBidirectional(product.remarkProperty());
            productParents.valueProperty().bindBidirectional(product.parentProperty());
        }
    }
    
    public boolean anyComponentChanged(){
        return !product.compares(productBackup);
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType, String string) {
        openDate.setDisable(true);
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
}
