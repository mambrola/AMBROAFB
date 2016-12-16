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
        
        items.keySet().stream().forEach((key) -> {
            String name = (getConverter() == null ) ? key.toString() : getConverter().toString(key);
            int count = items.get(key);
            CountComboBoxItem boxItem = new CountComboBoxItem(name);
            boxItem.itemNumberProperty().set(count);
            itemsMap.put(name, boxItem);
            
            System.out.println("name: " + name);
        });
        
        
        // ComboBox button cell text will show after any item select in comboBox. 
        // So we select every item. The last selected item will be zero indexed (only for visually effect).
        for (int i = getItems().size() - 1; i >= 0; i--){
            getSelectionModel().select(i);
        }
    }
    
    public Map<T, Integer> getData(){
        return comboBoxResult;
    }
    
    public boolean nothingIsSelected(){
        System.out.println("comboBoxResult.isEmpty(): " + comboBoxResult.isEmpty());
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
                    String boxItemName = boxItem.itemNameExpression().getValueSafe();
                    title = title.concat(boxItemName);
                    if (!boxItemName.isEmpty()){
                        title = title.concat(delimiter);
                    }
                }
                String titleStr = StringUtils.substringBeforeLast(title, delimiter);
                setText(titleStr);
                tooltip.setText(titleStr);
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
                CountComboBoxItem boxItem = itemsMap.get(name);
                if (boxItem == null){
                    System.out.println("name: " + name);
                    boxItem = new CountComboBoxItem(name);
                    itemsMap.put(name, boxItem);
                }
                
//                CountComboBoxItem boxItem = new CountComboBoxItem(name);
//                itemsMap.put(name, boxItem);
                
                boxItem.itemNumberProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    T itemForName = getItemFromData(name);
                    if (itemForName == null){ // If now select item that is not in firstly map, then program put it into map.
                        itemForName = item;
                    }
                    
                    if (newValue.intValue() > 0){
                        comboBoxResult.put(itemForName, newValue.intValue());
                    }
                    else {
                        if (comboBoxResult.containsKey(itemForName)){
                            comboBoxResult.remove(itemForName);
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
        
        // Returns item if it is into comboBoxResult map.
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
