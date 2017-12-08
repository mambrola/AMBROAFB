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
import ambroafb.general.NumberConverter;
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
public class ChargeUtilityDialogController extends DialogController {

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
    
    @FXML @ContentNotEmpty @ContentAmount
    private AmountField vat;
    
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        // Value change of utility also changes iso:
        utilities.valueProperty().addListener((ObservableValue<? extends DocMerchandise> observable, DocMerchandise oldValue, DocMerchandise newValue) -> {
            currency.setText(newValue.getIso());
            changeVatFieldValue(amount.getText());
        });
        
        amount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeVatFieldValue(newValue);
        });
    }
    
    private void changeVatFieldValue(String amount){
        String vatValue = "";
        if (amount != null && !amount.isEmpty()){
            Float amountValue = NumberConverter.stringToFloat(amount, 2);
            if (utilities.getValue() != null && amountValue != null){
                Float vatRate = utilities.getValue().getVatRate();
                Float percentResult = (amountValue * vatRate / 100);
                vatValue = NumberConverter.makeFloatStringBySpecificFraction(percentResult, 2);
            }
        }
        vat.setText(vatValue);
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            ChargeUtility chargeUtility = (ChargeUtility)object;
            utilities.valueProperty().bindBidirectional(chargeUtility.merchandiseProperty());
            docDate.valueProperty().bindBidirectional(chargeUtility.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(chargeUtility.docInDocDateProperty());
            currency.textProperty().bindBidirectional(chargeUtility.isoProperty());
            amount.textProperty().bindBidirectional(chargeUtility.amountProperty());
            vat.textProperty().bindBidirectional(chargeUtility.vatProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            ((ChargeUtility)sceneObj).setDocDate(LocalDate.now().toString());
            ((ChargeUtility)sceneObj).setDocInDocDate(LocalDate.now().toString());
            ((ChargeUtility)sceneObj).amountProperty().set("");
            backupObj.copyFrom(sceneObj);
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    @Override
    public void okayAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancelAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
