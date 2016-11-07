/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class MapEditor extends ComboBox<MapEditorElement> {
    
    private Map<String, MapEditorElement> itemsMap;
    private String delimiter;
    private Consumer<MapEditorElement> removeElement;
    private String keyPattern, valuePattern;
    
    public MapEditor(){
        this.setEditable(true);
        itemsMap = new HashMap<>();
        delimiter = " : ";
        keyPattern = ""; // (?<![\\d-])\\d+
        valuePattern = ""; // [0-9]{1,13}(\\.[0-9]*)?
        
        
        this.setCellFactory((ListView<MapEditorElement> param) -> new CustomCell());
        
        removeElement = (MapEditorElement elem) -> {
            if (itemsMap.containsKey(elem.getKey())){
                itemsMap.remove(elem.getKey());
                if (getValue() != null && getValue().compare(elem) == 0){
                    getEditor().setText(delimiter);
                }
                getItems().remove(elem);
            }
        };
        
        // Never hide comboBox items listView:
        this.setSkin(new ComboBoxListViewSkin(this){
            @Override
            protected boolean isHideOnClickEnabled(){
                return false;
            }
        });
        
        // Control textField input.
        TextField editor = getEditor();
        editor.setText(delimiter);
        editor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.isEmpty() || newValue.equals(delimiter)) {
                editor.setText(delimiter);
            } else if (!newValue.contains(delimiter)) {
                editor.setText(oldValue);
            } else {
                String keyInput = StringUtils.substringBefore(newValue, delimiter).trim();
                String valueInput = StringUtils.substringAfter(newValue, delimiter).trim();

                if (!keyInput.isEmpty() && !Pattern.matches(keyPattern, keyInput)) {
                    keyInput = StringUtils.substringBefore(oldValue, delimiter).trim();
                }
                if (!valueInput.isEmpty() && !Pattern.matches(valuePattern, valueInput)) {
                    valueInput = StringUtils.substringAfter(oldValue, delimiter).trim();
                }
                
                editor.setText(keyInput + delimiter + valueInput);
            }
        });
        
        setSelectionModel(new SingleSelectionModel<MapEditorElement>() {
            @Override
            protected MapEditorElement getModelItem(int index) {
                if (index >= 0 && index < getItems().size()){
                    MapEditorElement item = getItems().get(index);
                    return item;
                }
                return null;
            }

            @Override
            protected int getItemCount() {
                return getItems().size();
            }
        });
    }
    
    private class CustomCell extends ListCell<MapEditorElement> {

        public CustomCell() {
            super();
        }
        
        @Override
        public void updateItem(MapEditorElement item, boolean empty){
            super.updateItem(item, empty);
            if (item == null || empty){
                setGraphic(null);
            }
            else {
                MapEditorItem mapEditorItem = new MapEditorItem(item, delimiter, removeElement);
                itemsMap.put(item.getKey(), item);
                setGraphic(mapEditorItem);
            }
        }
    }
    
    @FXML
    public void setKeyPattern(String regex){
        keyPattern = regex;
    }
    
    @FXML
    public String getKeyPattern(){
        return keyPattern;
    }
    
    @FXML
    public void setValuePattern(String regex){
        valuePattern = regex;
    }
    
    @FXML
    public String getValuePattern(){
        return valuePattern;
    }
    
    @FXML
    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
        getEditor().setText(delimiter);
    }
    
    @FXML
    public String getDelimiter(){
        return delimiter;
    }
}
