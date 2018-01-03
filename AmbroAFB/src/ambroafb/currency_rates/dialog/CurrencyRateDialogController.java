/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.dialog;

import ambro.ADatePicker;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.currency_rates.CurrencyRate;
import ambroafb.general.editor_panel.EditorPanel.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.Annotations.ContentRate;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.scene_components.number_fields.rate_field.RateField;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
    
    @FXML @ContentNotEmpty @ContentPattern(value = "[1][0]*", explain = "anot_rate_unit_exp")
    private TextField count;
    
    @FXML @ContentNotEmpty @ContentRate
    private RateField rate;
    

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
            CurrencyRate currRate = (CurrencyRate)object;
            currRateDate.valueProperty().bindBidirectional(currRate.dateProperty());
            count.textProperty().bindBidirectional(currRate.countProperty());
            rate.textProperty().bindBidirectional(currRate.rateProperty());
            
//            currencies.valueProperty().bindBidirectional(currRate.currencyProperty());
        }
    }

    @Override
    protected void makeExtraActions(EDITOR_BUTTON_TYPE buttonType) {
        CurrencyRate currencyRate = (CurrencyRate) sceneObj;
        
        Consumer<ObservableList<Currency>> setIso = (currencyList) -> {
            String iso = currencyRate.getIso();
            Bindings.bindBidirectional(currencyRate.isoProperty(), currencies.valueProperty(), new IsoToCurrencyBiConverter());
            currencyRate.setIso(iso); // Bindings.bindBidirectional has priority value seter - firstly, right parameter value will set to left parameter. So, if object already has iso, it does not lose.
        };
        currencies.fillComboBoxWithoutALLAndWithoutRatesBasicIso(setIso);
    }

    
    private class IsoToCurrencyBiConverter extends StringConverter<Currency> {

        @Override
        public String toString(Currency curr) {
            String iso = null;
            if (curr != null){
                iso = curr.getIso();
            }
            return iso;
        }

        @Override
        public Currency fromString(String iso) {
            Currency currency = null;
            Optional<Currency> optCurrency = currencies.getItems().stream().filter((curr) -> Objects.equals(curr.getIso(), iso)).findFirst();
            if (optCurrency.isPresent()){
                currency = optCurrency.get();
            }
            return currency;
        }
        
    }
}
