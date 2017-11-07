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
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentAmount;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class PaymentUtilityDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private DocMerchandiseComboBox utilities;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private TextField currency;
    
    @FXML @ContentNotEmpty @ContentAmount
    private AmountField amount;
    
    @FXML
    private DialogOkayCancelController okayCancelController;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        // Value change of utility also changes iso:
        utilities.valueProperty().addListener((ObservableValue<? extends DocMerchandise> observable, DocMerchandise oldValue, DocMerchandise newValue) -> {
            currency.setText(newValue.getIso());
        });
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            PaymentUtility paymentUtility = (PaymentUtility)object;
            utilities.valueProperty().bindBidirectional(paymentUtility.utilityProperty());
            docDate.valueProperty().bindBidirectional(paymentUtility.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(paymentUtility.docInDocDateProperty());
            currency.textProperty().bindBidirectional(paymentUtility.isoProperty());
            amount.textProperty().bindBidirectional(paymentUtility.amountProperty()); // -1 default value not show in dialog, becouse of "amount" textfield value pattern.
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            ((PaymentUtility)sceneObj).setDocDate(LocalDate.now().toString());
            ((PaymentUtility)sceneObj).setDocInDocDate(LocalDate.now().toString());
            ((PaymentUtility)sceneObj).amountProperty().set("");
            backupObj.copyFrom(sceneObj);
        }
    }
    
    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
