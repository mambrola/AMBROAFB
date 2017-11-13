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
import ambroafb.general.GeneralConfig;
import ambroafb.general.NumberConverter;
import ambroafb.general.Printer;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class ConversionDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private IsoComboBox sellCurrency;
    @FXML @ContentNotEmpty
    private AccountComboBox sellAccount;
    @FXML @ContentNotEmpty @ContentAmount
    private AmountField sellAmount;
    
    @FXML
    private IsoComboBox buyingCurrency;
    @FXML @ContentNotEmpty
    private AccountComboBox buyingAccount;
    @FXML @ContentNotEmpty @ContentAmount
    private AmountField buyingAmount;
    @FXML
    private Label currentRateTitle;
    @FXML
    private Button currentRate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private boolean rateTopToBottomDirection = true;
    private final String purchaseRateTitle = GeneralConfig.getInstance().getTitleFor("purchase_rate");
    private final String inverseRateTitle = GeneralConfig.getInstance().getTitleFor("inverse_rate");

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        sellAccount.fillComboBoxWithoutALL(null);
        buyingAccount.fillComboBoxWithoutALL(null);
        
        sellCurrency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            sellAccount.filterBy(newValue);
            buyingCurrency.filterBy(newValue);
        });
        buyingCurrency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            buyingAccount.filterBy(newValue);
        });
        
        sellAmount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeRateText(newValue, buyingAmount.getText());
        });
        
        buyingAmount.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeRateText(sellAmount.getText(), newValue);
        });
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if(object != null){
            Conversion conversion = (Conversion)object;
            docDate.valueProperty().bindBidirectional(conversion.docDateProperty());
            docInDocDate.valueProperty().bindBidirectional(conversion.docInDocDateProperty());
            sellCurrency.valueProperty().bindBidirectional(conversion.sellCurrencyProperty());
            buyingCurrency.valueProperty().bindBidirectional(conversion.buyingCurrencyProperty());
            sellAccount.valueProperty().bindBidirectional(conversion.sellAccountProperty());
            buyingAccount.valueProperty().bindBidirectional(conversion.buyingAccountProperty());
            sellAmount.textProperty().bindBidirectional(conversion.sellAmountProperty());
            buyingAmount.textProperty().bindBidirectional(conversion.buyingAmountProperty());
        }
    }
    
    @FXML
    private void changeRateDirection(ActionEvent event){
        rateTopToBottomDirection = !rateTopToBottomDirection;
        changeRateText(sellAmount.getText(), buyingAmount.getText());
    }
    
    private void changeRateText(String sellAmountValue, String buyingAmountValue){
        String rateResult = "";
        if (sellAmountValue != null && !sellAmountValue.isEmpty() && buyingAmountValue != null && !buyingAmountValue.isEmpty()){
            Float sellAmountFloat = NumberConverter.stringToFloat(sellAmountValue, 2);
            Float buyingAmountFloat = NumberConverter.stringToFloat(buyingAmountValue, 2);
            if (sellAmountFloat != null && buyingAmountFloat != null && 
                sellAmountFloat > 0 && buyingAmountFloat > 0){
                Float amountsRate = (rateTopToBottomDirection) ? buyingAmountFloat / sellAmountFloat : sellAmountFloat / buyingAmountFloat;
                rateResult = NumberConverter.makeFloatStringBySpecificFraction(amountsRate, 4);
            }
        }
        String rateTitle = (rateTopToBottomDirection) ? purchaseRateTitle : inverseRateTitle;
        currentRateTitle.setText(rateTitle);
        currentRate.setText(rateResult);
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            System.out.println("--------- ADD BY SAMPLE -----------");
            Printer.printIsNull("((Conversion)sceneObj).getSellAccount()", ((Conversion)sceneObj).getSellAccount());
            System.out.println(((Conversion)sceneObj).getSellAccount());
            
            
            ((Conversion)sceneObj).setDocDate(LocalDate.now().toString());
            ((Conversion)sceneObj).setDocInDocDate(LocalDate.now().toString());
            ((Conversion)sceneObj).sellAmountProperty().set(""); // for empty amount field.
            ((Conversion)sceneObj).buyingAmountProperty().set("");
            backupObj.copyFrom(sceneObj);
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
