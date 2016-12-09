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
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private final ObjectProperty<Map<T, Integer>> comboBoxResult;
    private Consumer<T> consumer;
    
    public CountComboBox(){
        itemsMap = new HashMap<>();
        tooltip = new Tooltip();
        comboBoxResult = new SimpleObjectProperty<>(new HashMap<>());
        
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
    
    public void setConsumer(Consumer<T> consumer){
        this.consumer = consumer;
    }
    
    public void setData(Map<T, Integer> items){
        System.out.println("setData. tems.size(): " + items.size());
        comboBoxResult.set(items);
//        items.keySet().stream().forEach((key) -> {
//            System.out.println("key: " + key.toString());
//            
//            int value = items.get(key);
//            comboBoxResult.get().put(key, value);
//        });
    }
    
//    public ObjectProperty<Map<T, Integer>> resultProperty(){
//        return comboBoxResult;
//    }
    
    public boolean nothingIsSelected(){
        return comboBoxResult.get().isEmpty();
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
                
                comboBoxResult.get().keySet().forEach((key) -> {
                    String saveItemName = (box.getConverter() == null) ? key.toString() : box.getConverter().toString(key);;
                    if (saveItemName.equals(name)){
                        boxItem.itemNumberProperty().set(comboBoxResult.get().get(key));
                    }
                });
                
                // ComboBox button cell text will show after any item select in comboBox. 
                //So we select every item. The last selected item will be zero indexed (only for visually effect).
                int index = box.getItems().indexOf(item);
                int size = box.getItems().size();
                box.getSelectionModel().select((index + 1) % size);
                
                boxItem.itemNumberProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    Map<T, Integer> elems = comboBoxResult.get();
                    if (newValue.intValue() > 0){
                        elems.put(item, boxItem.itemNumberProperty().get());
                    }
                    else {
                        if (elems.containsKey(item)){
                            elems.remove(item);
                        }
                    }
                    
                    if (consumer != null){
                        consumer.accept(item);
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
    }
}
