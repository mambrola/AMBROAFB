/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.beans.value.ObservableValue;
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
    
    private final Map<String, CountComboBoxItem> itemsMap;
    private final Tooltip tooltip;
    private Map<T, Integer> comboBoxResult;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        comboBoxResult = new HashMap<>();
        
        this.setPrefWidth(500);
        this.setButtonCell(new ComboBoxCustomButtonCell());
        this.setCellFactory((ListView<T> param) -> new ComboBoxCustomCell(this));
        this.setTooltip(tooltip);
        
        // Never hide comboBox items listView:
        this.setSkin(new ComboBoxListViewSkin(this){
            @Override
            protected boolean isHideOnClickEnabled(){
                return false;
            }
        });
    }
    
    public void setCustomConverter(StringConverter<T> converter){
        if (converter != null){
            this.setConverter(converter);
        }
    }
    
    public void setData(Map<T, Integer> items){
        comboBoxResult = items;
    }
    
    public Map<T, Integer> getData(){
        return comboBoxResult;
    }
    
    public boolean nothingIsSelected(){
        return comboBoxResult.isEmpty();
    }
    
    private class ComboBoxCustomButtonCell extends ListCell<T> {

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
                    CountComboBoxItem boxItem = itemsMap.get(key);
                    title = title.concat(boxItem.itemNameExpression().getValueSafe());
                    if (!boxItem.itemNameExpression().getValueSafe().isEmpty()){
                        title = title.concat(delimiter);
                    }
                }
                setText(StringUtils.substringBeforeLast(title, delimiter));
            }
        }
        
    }
    
    private class ComboBoxCustomCell extends ListCell<T> {

        private final ComboBox box;

        public ComboBoxCustomCell(ComboBox box) {
            super();
            this.box = box;
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                String name = (box.getConverter() == null) ? item.toString() : box.getConverter().toString(item);
                CountComboBoxItem boxItem = new CountComboBoxItem(name);
                itemsMap.put(name, boxItem);
                
                comboBoxResult.keySet().forEach((key) -> {
                    String saveItemName = (box.getConverter() == null) ? key.toString() : box.getConverter().toString(key);;
                    if (saveItemName.equals(name)){
                        boxItem.itemNumberProperty().set(comboBoxResult.get(key));
                    }
                });
                
                // ComboBox button cell text will show after any item select in comboBox. 
                //So we select every item. The last selected item will be zero indexed (only for visually effect).
                int index = box.getItems().indexOf(item);
                int size = box.getItems().size();
                box.getSelectionModel().select((index + 1) % size);
                
                boxItem.itemNumberProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    T itemForName = getItemFromData(name);
                    if (itemForName == null){ // If now select item that is not in firstly map, then program put it into map.
                        itemForName = item;
                    }
                    
                    if (newValue.intValue() > 0){
                        comboBoxResult.put(itemForName, boxItem.itemNumberProperty().get());
                    }
                    else {
                        if (comboBoxResult.containsKey(itemForName)){
                            comboBoxResult.remove(itemForName);
                            System.out.println("combBoxResult size: " + comboBoxResult.size());
                        }
                    }
                    
                });
                
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
        
        // Returns item if it is comboBoxResult map.
        private T getItemFromData(String name){
            for (T key : comboBoxResult.keySet()){
                String saveItemName = (box.getConverter() == null) ? key.toString() : box.getConverter().toString(key);;
                if (saveItemName.equals(name)){
                    return key;
                }
            }
            return null;
        }
    }
}
