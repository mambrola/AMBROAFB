/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Annotations.ContentMapEditor;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.mapeditor.MapEditor;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.general.scene_components.number_fields.integer_field.IntegerField;
import ambroafb.products.Product;
import ambroafb.products.ProductsSpecificsComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ProductDialogController extends DialogController {

    @FXML
    private VBox formPane;
    @FXML @ContentNotEmpty
    private TextField abbreviation;
    @FXML @ContentNotEmpty @ContentPattern(value = "[0-9]{2}", explain = "The content length must be 2.")
    private TextField former;
    @FXML @ContentNotEmpty
    private TextField descrip;
    @FXML @ContentNotEmpty
    private ProductsSpecificsComboBox specifics;
    @FXML @ContentNotEmpty @ContentPattern(value = AmountField.PRODUCT_PRICE_PATTERN, explain = AmountField.INCORRECT_CONTENT_EXPLAIN)
    private AmountField price;
    @FXML @ContentNotEmpty
    private CurrencyComboBox currencies;
    @FXML @ContentMapEditor(explainKey = "Left number must be days counter.", explainValue = "Right number must be discount percent. Exp: 4.25")
    private MapEditor discounts;
    @FXML
    private CheckBox isAlive;
    @FXML
    private IntegerField maxCount, testingDays;
    
    
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
            Product product = (Product) object;
            abbreviation.textProperty().bindBidirectional(product.abbreviationProperty());
            former.textProperty().bindBidirectional(product.formerProperty());
            descrip.textProperty().bindBidirectional(product.descriptionProperty());
            specifics.valueProperty().bindBidirectional(product.specificProperty());
            price.textProperty().bindBidirectional(product.priceProperty());
            currencies.valueProperty().bindBidirectional(product.currencyProperty());
            discounts.setItems(product.getDiscountsForMapEditor());
            maxCount.textProperty().bindBidirectional(product.notJurMaxCountProperty());
            isAlive.selectedProperty().bindBidirectional(product.isAliveProperty());
            testingDays.textProperty().bindBidirectional(product.testingDaysProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanelable sceneObject, Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            discounts.changeState(true);
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
