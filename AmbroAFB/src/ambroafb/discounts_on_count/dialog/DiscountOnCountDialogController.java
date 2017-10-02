/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.general.scene_components.number_fields.integer_field.IntegerField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class DiscountOnCountDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private IntegerField licenseCount;
    @FXML @ContentNotEmpty 
    @ContentPattern(value = AmountField.FINALY_CONTENT_PATTERN, explain = "Pattern is incorrect (ex:  10.5)")
    private AmountField discountRate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private DiscountOnCount discCount, discCountBackup;
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
//        Utils.validateTextFieldContentListener(licenseCount, "(^[1-9][0-9]*)?");
//        Utils.validateTextFieldContentListener(discountRate, "(^0|[1-9][0-9]*)?([.]|[.][0-9]{1,2})?");
        permissionToClose = true;
    }    

    public void bindDiscountOnCount(DiscountOnCount discountOnCount) {
        this.discCount = discountOnCount;
        if (discountOnCount != null){
            licenseCount.textProperty().bindBidirectional(discountOnCount.licenseCountProperty());
            discountRate.textProperty().bindBidirectional(discountOnCount.discountRateProperty());
        }
    }

    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    /**
     * Disables all fields on Dialog stage.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    public void setBackupDiscountOnCount(DiscountOnCount discountOnCountBackup) {
        this.discCountBackup = discountOnCountBackup;
    }

    public boolean anyComponentChanged(){
        return !discCount.compares(discCountBackup);
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
