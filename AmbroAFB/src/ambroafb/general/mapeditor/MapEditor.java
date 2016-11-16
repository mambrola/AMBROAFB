/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class MapEditor extends ComboBox<MapEditorElement> {
    
    private Map<String, MapEditorElement> itemsMap;
    private String delimiter;
    private Consumer<MapEditorElement> removeElement, editElement;
    private String keyPattern, valuePattern, initializeClass, keySpecChars, valueSpecChars;
    private Class<?> initialize;
    private BooleanProperty incorrectElem = new SimpleBooleanProperty(false);
    
    public MapEditor(){
        this.setEditable(true);
        itemsMap = new HashMap<>();
        delimiter = " : "; // default value of delimiter
        keyPattern = ""; // (?<![\\d-])\\d+
        valuePattern = ""; // [0-9]{1,13}(\\.[0-9]*)?
        keySpecChars = "";
        valueSpecChars = "";
        
        
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
        
        editElement = (MapEditorElement elem) -> {
            getSelectionModel().select(-1);
            getEditor().setText(elem.getKey() + delimiter + elem.getValue());
            itemsMap.remove(elem.getKey());
            getItems().remove(elem);
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
        
        this.setConverter(new StringConverter<MapEditorElement>() {
            @Override
            public String toString(MapEditorElement object) {
                if (object == null){
                    return delimiter;
                }
                return object.getKey() + delimiter + object.getValue();
            }

            @Override
            public MapEditorElement fromString(String input) {
                MapEditorElement result = null;
                if (input != null && input.contains(delimiter)){
                    result = getNewInstance();
                    if (result == null) return null;
                    String keyInput = StringUtils.substringBefore(input, delimiter).trim();
                    String valueInput = StringUtils.substringAfter(input, delimiter).trim();
                    if (!keyInput.isEmpty()){
                        result.setKey(keyInput);
                    }
                    if (!valueInput.isEmpty()){
                        result.setValue(valueInput);
                    }
                    boolean keyOutOfSpec = keySpecChars.isEmpty() || !StringUtils.containsOnly(result.getKey(), keySpecChars);
                    boolean valueOutOfSpec = valueSpecChars.isEmpty() || !StringUtils.containsOnly(result.getValue(), valueSpecChars);
                    if (!keyInput.isEmpty() && !valueInput.isEmpty() && !itemsMap.containsKey(keyInput) &&
                        (keyOutOfSpec && valueOutOfSpec)){
                        itemsMap.put(keyInput, result);
                        getItems().add(result);
                        return null;
                    }
                }
                return result;
            }
        });
        
        // Control caret position in textField.
        editor.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            int caretOldPos = editor.getCaretPosition();
            int delimiterIndex = editor.getText().indexOf(delimiter);
            if (event.getCode().equals(KeyCode.RIGHT)) {
                if (caretOldPos + 1 > delimiterIndex && caretOldPos + 1 <= delimiterIndex + delimiter.length()) {
                    editor.positionCaret(delimiterIndex + delimiter.length());
                    event.consume();
                }
            } else if (event.getCode().equals(KeyCode.LEFT)) {
                if (caretOldPos - 1 >= delimiterIndex && caretOldPos - 1 < delimiterIndex + delimiter.length()) {
                    editor.positionCaret(delimiterIndex);
                    event.consume();
                }
            }
        });
    }
    
    private MapEditorElement getNewInstance() {
        MapEditorElement result = null;
        try {
            result = (MapEditorElement) initialize.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MapEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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
                MapEditorItem mapEditorItem = new MapEditorItem(item, delimiter, removeElement, editElement);
                itemsMap.put(item.getKey(), item);
                setGraphic(mapEditorItem);
            }
        }
    }
    
    public BooleanProperty incorrectElemProperty(){
        return incorrectElem;
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
    public void setKeySpecChars(String keySpec){
        keySpecChars = keySpec;
    }
    
    @FXML
    public String getKeySpecChars(){
        return keySpecChars;
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
    public void setValueSpecChars(String valueSpec){
        valueSpecChars = valueSpec;
    }
    
    @FXML
    public String getValueSpecChars(){
        return valueSpecChars;
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
    
    @FXML
    public void setInitialize(String classStr){
        initializeClass = classStr;
        try {
            initialize = Class.forName(classStr);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MapEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public String getInitialize(){
        return initializeClass;
    }
}
