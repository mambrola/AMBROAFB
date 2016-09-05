/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.countcombobox;

import ambroafb.products.Product;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.beans.binding.StringExpression;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class CountComboBox extends ComboBox<Product> {
    
    private final Map<String, StringExpression> itemsMap;
    private final Tooltip tooltip;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        
        this.setPrefWidth(500);
        this.setConverter(new Converter());
        this.setButtonCell(new ComboBoxCustomButtonCell());
        this.setCellFactory((ListView<Product> param) -> new ComboBoxCustomCell(this));
        this.setTooltip(tooltip);
        
        // Never hide comboBox items listView:
        this.setSkin(new ComboBoxListViewSkin(this){
            @Override
            protected boolean isHideOnClickEnabled(){
                return false;
            }
        });
    }
    
    private class Converter extends StringConverter<Product> {

        @Override
        public String toString(Product object) {
            return object.toString();
        }

        @Override
        public Product fromString(String description) {
            Product product = new Product();
            product.setDescrip(description);
            return product;
        }
        
    }
    
    private class ComboBoxCustomButtonCell extends ListCell<Product> {

        private final String delimiter = ", ";
        
        public ComboBoxCustomButtonCell() {
            super();
        }

        @Override
        public void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                String title = "";
                SortedSet<String> sortedKeys = new TreeSet<>(itemsMap.keySet());
                for (String key : sortedKeys) {
                    title = title.concat(itemsMap.get(key).getValueSafe());
                    if (!itemsMap.get(key).getValueSafe().isEmpty()){
                        title = title.concat(delimiter);
                    }
                }
                setText(StringUtils.substringBeforeLast(title, delimiter));
            }
        }
        
    }
    
    private class ComboBoxCustomCell extends ListCell<Product> {

        private final ComboBox box;

        public ComboBoxCustomCell(ComboBox box) {
            super();
            this.box = box;
        }

        @Override
        public void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                String name = getConverter().toString(item);
                CountComboBoxItem boxItem = new CountComboBoxItem(name);
                itemsMap.put(name, boxItem.itemNameExpression());
                
                // ComboBox item could not select twise, so make this kind of action:
                boxItem.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
                    int selected = box.getSelectionModel().getSelectedIndex();
                    if (selected == box.getItems().size() - 1) {
                        box.getSelectionModel().selectPrevious();
                        box.getSelectionModel().selectNext();
                    } else {
                        box.getSelectionModel().selectNext();
                        box.getSelectionModel().selectPrevious();
                    }
                });
                setGraphic(boxItem);
            }
        }
    }
}
