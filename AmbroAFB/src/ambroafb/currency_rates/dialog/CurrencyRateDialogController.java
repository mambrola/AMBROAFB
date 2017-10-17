/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambro.ADatePicker;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.currency_rates.CurrencyRate;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.Annotations.ContentRate;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.rate_field.RateField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyRateDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML @ContentNotEmpty
    private ADatePicker currRateDate;
    
    @FXML @ContentNotEmpty
    private CurrencyComboBox currencies;
    
    @ContentNotEmpty @ContentPattern(value = "[1][0]*", explain = "anot_rate_unit_exp")
    @FXML 
    private TextField count;
    
    @FXML @ContentNotEmpty @ContentRate
    private RateField rate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        currencies.fillComboBoxWithoutALLAndWithoutRatesBasicIso(null);
    }
    
    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            CurrencyRate currRate = (CurrencyRate)object;
            currRateDate.valueProperty().bindBidirectional(currRate.dateProperty());
            currencies.valueProperty().bindBidirectional(currRate.currencyProperty());
            count.textProperty().bindBidirectional(currRate.countProperty());
            rate.textProperty().bindBidirectional(currRate.rateProperty());
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
