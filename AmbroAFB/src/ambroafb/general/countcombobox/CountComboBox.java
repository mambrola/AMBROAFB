/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @param <T>
 */
public class CountComboBox<T> extends ComboBox<T> {
    
    private final Map<String, IntegerProperty> itemsMap;
    private final Tooltip tooltip;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        
        this.setPrefWidth(500);
        this.setConverter(new Converter<>());
        this.setButtonCell(new ComboBoxCustomButtonCell<>());
        this.setCellFactory((ListView<T> param) -> new ComboBoxCustomCell<>(this));
        this.setTooltip(tooltip);
    }
    
    private class Converter<T> extends StringConverter<T> {

        @Override
        public String toString(T object) {
            return object.toString();
        }

        @Override
        public T fromString(String string) {
            T result = null;
            try {
                result = (T) getClass().getConstructor(String.class).newInstance(string);
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(CountComboBox.class.getName()).log(Level.SEVERE, null, ex);
            }
            return result;
        }
        
    }
    
    private class ComboBoxCustomButtonCell<T> extends ListCell<T> {

        private final String delimiter = ", ";
        
        public ComboBoxCustomButtonCell() {
            super();
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                String oldText = getText();
                String itemName = item.toString();
                int itemCount = itemsMap.get(item.toString()).get();
                String newText = oldText;
                if (oldText.contains(itemName)) {
                    String afterPartOfItemName = StringUtils.substringAfter(oldText, itemName + delimiter);
                    String beforePartOfItemName = StringUtils.substringBefore(oldText, "-" + itemName);
                    String beforePartWithoutItemCount = "";
                    if (beforePartOfItemName.lastIndexOf(delimiter) != -1){
                        beforePartWithoutItemCount = beforePartOfItemName.substring(0, beforePartOfItemName.lastIndexOf(delimiter) + delimiter.length());
                    }
                    // before previous and next selection, title text may does not change and stay the same.
                    if (("" + itemCount).equals(StringUtils.substringAfterLast(beforePartOfItemName, delimiter)))
                        return;
                    if (itemCount == 0) {
                        newText = beforePartWithoutItemCount + afterPartOfItemName;
                    } else {
                        newText = beforePartWithoutItemCount + itemCount + "-" + itemName + delimiter + afterPartOfItemName;
                    }
                } else if (itemCount != 0) {
                    newText = oldText + itemCount + "-" + item.toString() + delimiter;
                }
                setText(newText);
                tooltip.setText(newText);
            }
        }
        
    }
    
    private class ComboBoxCustomCell<T> extends ListCell<T> {

        private final ComboBox box;

        public ComboBoxCustomCell(ComboBox box) {
            super();
            this.box = box;
        }

        @Override
        public void updateItem(T item, boolean empty) {
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
