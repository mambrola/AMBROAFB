/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentAmount;
import ambroafb.general.interfaces.Annotations.ContentMapEditor;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.mapeditor.MapEditor;
import ambroafb.general.mapeditor.MapEditorElement;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import ambroafb.general.scene_components.number_fields.integer_field.IntegerField;
import ambroafb.products.Product;
import ambroafb.products.ProductsSpecificsComboBox;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
    @FXML @ContentNotEmpty @ContentPattern(value = "[0-9]{2}", explain = "anot_former_exp")
    private TextField former;
    @FXML @ContentNotEmpty
    private TextField descrip;
    @FXML @ContentNotEmpty
    private ProductsSpecificsComboBox specifics;
    @FXML @ContentNotEmpty @ContentAmount(integerPartMaxLen = 8)
    private AmountField price;
    @FXML @ContentNotEmpty
    private CurrencyComboBox currencies;
    @FXML @ContentMapEditor
    private MapEditor discounts;
    @FXML
    private CheckBox isAlive;
    @FXML
    private IntegerField maxCount, testingDays;
    
    
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
            Product product = (Product) object;
            abbreviation.textProperty().bindBidirectional(product.abbreviationProperty());
            former.textProperty().bindBidirectional(product.formerProperty());
            descrip.textProperty().bindBidirectional(product.descriptionProperty());
//            specifics.valueProperty().bindBidirectional(product.specificProperty());
            price.textProperty().bindBidirectional(product.priceProperty());
//            currencies.valueProperty().bindBidirectional(product.currencyProperty());
            maxCount.textProperty().bindBidirectional(product.notJurMaxCountProperty());
            isAlive.selectedProperty().bindBidirectional(product.isAliveProperty());
            testingDays.textProperty().bindBidirectional(product.testingDaysProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.DELETE)){
            discounts.changeState(true);
        }
        Product product = ((Product)sceneObj);
        List<ProductDiscount> productDiscounts = product.getDiscounts();
        productDiscounts.forEach((discount) -> {
            discounts.getItems().add(discount);
        });
        createListListener(discounts.getItems(), productDiscounts);
        
        Consumer<ObservableList<ProductSpecific>> selectSpecific = (currencyList) -> {
            Integer specificId = product.getSpecific();
            Bindings.bindBidirectional(product.specificIdProperty(), specifics.valueProperty(), new SpecificToIdBiConverter());
            product.setSpecific(specificId);
        };
        specifics.fillComboBox(selectSpecific);
        
        Consumer<ObservableList<Currency>> selectCurrency = (currencyList) -> {
            String currecyIso = product.getIso();
            Bindings.bindBidirectional(product.isoProperty(), currencies.valueProperty(), new CurrencyToIsoBiConverter());
            product.setIso(currecyIso);
        };
        currencies.fillComboBoxWithoutALLAndWithBasicIso(selectCurrency);
    }


    private void createListListener(ObservableList<MapEditorElement> items, List<ProductDiscount> productDiscounts) {
        items.addListener((ListChangeListener.Change<? extends MapEditorElement> c) -> {
            c.next();
            if (c.wasAdded()) {
                List<? extends Object> adds = c.getAddedSubList();
                adds.stream().forEach((elem) -> {
                    ProductDiscount disc = (ProductDiscount) elem;
                    if (disc != null && !productDiscounts.contains(disc)) {
                        productDiscounts.add(disc);
                    }
                });
            } else if (c.wasRemoved()) {
                List<? extends Object> removes = c.getRemoved();
                removes.stream().forEach((elem) -> {
                    ProductDiscount disc = (ProductDiscount) elem;
                    if (disc != null && productDiscounts.contains(disc)) {
                        productDiscounts.remove(disc);
                    }
                });
            }
        });
    }


    @Override
    protected void removeListeners() {
    }

    
    private class SpecificToIdBiConverter extends StringConverter<ProductSpecific> {

        @Override
        public String toString(ProductSpecific prodSpecific) {
            String id = null;
            if (prodSpecific != null) {
                id = "" + prodSpecific.getProductSpecificId();
            }
            return id;
        }

        @Override
        public ProductSpecific fromString(String id) {
            ProductSpecific prodSpec = null;
            Optional<ProductSpecific> optProductSpec = specifics.getItems().stream().filter((spec) -> Objects.equals(id, "" + spec.getProductSpecificId())).findFirst();
            if (optProductSpec.isPresent()){
                prodSpec = optProductSpec.get();
            }
            return prodSpec;
        }
    }
    
    
    private class CurrencyToIsoBiConverter extends StringConverter<Currency> {

        @Override
        public String toString(Currency currency) {
            String iso = null;
            if (currency != null) {
                iso = currency.getIso();
            }
            return iso;
        }

        @Override
        public Currency fromString(String iso) {
            Currency currency = null;
            Optional<Currency> optCurrency = currencies.getItems().stream().filter((curr) -> Objects.equals(iso, curr.getIso())).findFirst();
            if (optCurrency.isPresent()) {
                currency = optCurrency.get();
            }
            return currency;
        }
    }
}
