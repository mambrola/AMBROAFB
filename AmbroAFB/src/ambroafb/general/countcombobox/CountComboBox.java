/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @param <T>
 */
public class CountComboBox<T> extends ComboBox<T> {
    
    private final Map<String, StringExpression> itemsMap;
    private final Tooltip tooltip;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        
        this.setPrefWidth(500);
        this.setConverter(new Converter<>());
        this.setButtonCell(new ComboBoxCustomButtonCell<>());
        this.setCellFactory((ListView<T> param) -> new ComboBoxCustomCell<>(this));
        this.setTooltip(tooltip);
        this.setSkin(new ComboBoxListViewSkin(this){
            @Override
            protected boolean isHideOnClickEnabled(){
                return false;
            }
        });
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
                String name = item.toString();
                CountComboBoxItem boxItem = new CountComboBoxItem(name);
                itemsMap.put(name, boxItem.itemNameExpression());
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
