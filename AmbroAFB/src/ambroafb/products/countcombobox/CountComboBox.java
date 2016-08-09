/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.countcombobox;

import ambroafb.products.Product;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
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
    
    private final Map<String, IntegerProperty> itemsMap;
    private final Tooltip tooltip;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        
        this.setPrefWidth(500);
        this.setConverter(new Converter());
        this.setButtonCell(new ComboBoxCustomButtonCell());
        this.setCellFactory((ListView<Product> param) -> new ComboBoxCustomCell(this));
        this.setTooltip(tooltip);
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
                String oldText = getText();
                String itemName = item.toString();
                int itemCount = itemsMap.get(item.toString()).get();
                String newText = oldText;
                if (oldText.contains(itemName)) {
                    String afterPartOfItemName = StringUtils.substringAfter(oldText, itemName);
                    String beforePartOfItemName = StringUtils.substringBefore(oldText, "-" + itemName);
                    String beforePartWithoutItemCount = "";
                    if (beforePartOfItemName.lastIndexOf(delimiter) != -1){
                        beforePartWithoutItemCount = beforePartOfItemName.substring(0, beforePartOfItemName.lastIndexOf(delimiter) + delimiter.length());
                    }
                    if (itemCount == 0) {
                        if (beforePartWithoutItemCount.isEmpty())
                            afterPartOfItemName = StringUtils.substringAfter(afterPartOfItemName, delimiter);
                        else 
                            beforePartWithoutItemCount = StringUtils.substringBeforeLast(beforePartOfItemName, delimiter);
                        newText = beforePartWithoutItemCount + afterPartOfItemName;
                    } else {
                        newText = beforePartWithoutItemCount + itemCount + "-" + itemName + afterPartOfItemName;
                    }
                } else if (itemCount != 0) {
                    if (!oldText.isEmpty()) 
                        oldText = oldText + delimiter;
                    newText = oldText + itemCount + "-" + item.toString();
                }
                setText(newText);
                tooltip.setText(newText);
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
                CountComboBoxItem boxItem = new CountComboBoxItem();
                boxItem.setItemName(item.toString()); // getConverter().toString(item)
                boxItem.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
                    box.show();
                    int selected = box.getSelectionModel().getSelectedIndex();
                    if (selected == box.getItems().size() - 1) {
                        box.getSelectionModel().selectPrevious();
                        box.getSelectionModel().selectNext();
                    } else {
                        box.getSelectionModel().selectNext();
                        box.getSelectionModel().selectPrevious();
                    }
                });
                itemsMap.put(item.toString(), boxItem.itemNumberProperty());
                setGraphic(boxItem);
            }
        }
    }
}
