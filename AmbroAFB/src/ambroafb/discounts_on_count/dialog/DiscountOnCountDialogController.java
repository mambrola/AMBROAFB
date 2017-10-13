/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.general.scene_components.number_fields.integer_field.IntegerField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class DiscountOnCountDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private IntegerField licenseCount;
    @FXML @ContentNotEmpty 
    @ContentPattern(value = AmountField.AMOUNT_PATTERN, explain = AmountField.INCORRECT_CONTENT_EXPLAIN)
    private AmountField discountRate;
    
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
            DiscountOnCount discountOnCount = (DiscountOnCount) object;
            licenseCount.textProperty().bindBidirectional(discountOnCount.licenseCountProperty());
            discountRate.textProperty().bindBidirectional(discountOnCount.discountRateProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanelable sceneObject, EDITOR_BUTTON_TYPE buttonType) {
        
    }
    
    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
}
