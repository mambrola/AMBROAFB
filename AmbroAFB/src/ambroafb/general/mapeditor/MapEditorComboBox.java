/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class MapEditorComboBox<T> extends ComboBox<T>{
    
    private final Map<String, String> elements = new HashMap<>();
    private String delimiter = " : ";
    private String keyPattern, valuePattern;
    private final SortedList sortedList;
    private final ObservableList<T> comboBoxItemsList;
    private String targetClassName;
    private Class<?> targetClass;
    
    public MapEditorComboBox() {
        this.setEditable(true);
        TextField editor = getEditor();
        editor.setText(delimiter);
        comboBoxItemsList = getItems();
        this.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T elem) {
                return (elem == null) ? delimiter : elem.toString();
            }

            @Override
            public T fromString(String input) {
                String key = StringUtils.substringBefore(input, delimiter).trim();
                String value = StringUtils.substringAfter(input, delimiter).trim();

                int selectedIndex = getSelectionModel().getSelectedIndex();
                T selectedItem = getSelectionModel().getSelectedItem();
                String selectedItemToString = "";
                if (selectedItem != null) {
                    selectedItemToString = selectedItem.toString();
                }

                if (selectedIndex < 0) { // If nothing is selected then user want to add new item.
                    if(key.isEmpty() || value.isEmpty() || elements.containsKey(key)){
                        selectedItem = getNewInstance(key, value);
                    }
                    else {
                        T newItem = getNewInstance(key, value);
                        comboBoxItemsList.add(newItem);
                        elements.put(key, value);
                    }
                } else {
                    String itemKey = (selectedItemToString.isEmpty()) ? "" : selectedItemToString.substring(0, selectedItemToString.indexOf(delimiter)).trim();
                    String itemValue = (selectedItemToString.isEmpty()) ? "" : selectedItemToString.substring(selectedItemToString.indexOf(delimiter) + delimiter.length()).trim();

                    if (input.equals(delimiter)) { // If remove selected item, then remove from structures.
                        elements.remove(itemKey);
                        comboBoxItemsList.remove(selectedItem);
                        setValue(null);
                    } else if (key.isEmpty() || value.isEmpty()) { // If key or value is not fill.
                        selectedItem = getNewInstance(key, value); // Don't disappare input.
                    } else { // If user wants to update item.
                        boolean updateOnlyKey = !key.equals(itemKey) && !elements.containsKey(key);
                        boolean updateOnlyValue = key.equals(itemKey) && !value.equals(itemValue);
                        if (updateOnlyKey || updateOnlyValue){
                            elements.remove(itemKey); // remove old key from map
                            comboBoxItemsList.remove(selectedItem);
                            T newItem = getNewInstance(key, value);
                            comboBoxItemsList.add(newItem);
                            elements.put(key, value);
                            setValue(null); // disappare input in textField and deselect item value/index.
                            selectedItem = null;
                        }
                    }
                }
                return selectedItem;
            }

            private T getNewInstance(String key, String value) {
                Object result = null;
                try {
                    result = targetClass.getConstructor(String.class, String.class).newInstance(key, value);
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MapEditorComboBox.class.getName()).log(Level.SEVERE, null, ex);
                }
                return (T) result;
            }
        });

        // create SortedList with target list and Comparator logic:
        sortedList = new SortedList<>(comboBoxItemsList, (T elem1, T elem2) -> {
            String elem1ToString = elem1.toString();
            String elem2ToString = elem2.toString();
            int key1 = Integer.parseInt(elem1ToString.substring(0, elem1ToString.indexOf(delimiter)));
            int key2 = Integer.parseInt(elem2ToString.substring(0, elem2ToString.indexOf(delimiter)));
            return key1 - key2;
        });
        this.setItems(sortedList);

        // Control textField input.
        editor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.isEmpty() || newValue.equals(delimiter)) {
                editor.setText(delimiter);
            } else if (!newValue.contains(delimiter)) {
                editor.setText(oldValue);
            } else {
                String keyInput = StringUtils.substringBefore(newValue, delimiter).trim();
                String valueInput = StringUtils.substringAfter(newValue, delimiter).trim();
                System.out.println("keyPattern: " + keyPattern);
                if (!Pattern.matches(keyPattern, keyInput)) {
                    keyInput = StringUtils.substringBefore(oldValue, delimiter).trim();
                }
                if (!Pattern.matches(valuePattern, valueInput)) {
                    valueInput = StringUtils.substringAfter(oldValue, delimiter).trim();
                }
                editor.setText(keyInput + delimiter + valueInput);
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
    
    public void setItemsCustom(ObservableList<T> items){
        items.stream().forEach((elem) -> {
            String toString = elem.toString();
            String key = StringUtils.substringBefore(toString, delimiter).trim();
            String value = StringUtils.substringAfter(toString, delimiter).trim();
            elements.put(key, value);
            comboBoxItemsList.add(elem);
        });
        comboBoxItemsList.addListener((ListChangeListener.Change<? extends T> c) -> {
            c.next();
            if (c.wasAdded()){
                T elem = c.getAddedSubList().get(c.getAddedSize() - 1);
                if (!items.contains(elem)){ // must be equal methods in T class.
                    System.out.println("add c: " + c.toString());
                    items.add(elem);
                }
            }
            else if (c.wasRemoved()){
                T elem = c.getRemoved().get(c.getRemovedSize() - 1);
                if (items.contains(elem)){
                    System.out.println("remove c: " + c.toString());
                    items.remove(elem);
                }
            }
        });
        items.addListener((ListChangeListener.Change<? extends T> c) -> {
            c.next();
            if (c.wasAdded()){
                T elem = c.getAddedSubList().get(c.getAddedSize() - 1);
                comboBoxItemsList.add(elem);
            }
            else if (c.wasRemoved()){
                T elem = c.getRemoved().get(c.getRemovedSize() - 1);
                comboBoxItemsList.remove(elem);
            }
            else if (c.wasUpdated()){
                System.out.println("update in user list");
            }
        });
    }

    // Setters and getters for pattern. The also use from fxml.
    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }
    
    public String getDelimiter(){
        return delimiter;
    }
    
    public void setKeyPattern(String keyPattern) {
        this.keyPattern = keyPattern;
    }

    public String getKeyPattern() {
        return keyPattern;
    }

    public void setValuePattern(String valuePattern) {
        this.valuePattern = valuePattern;
    }

    public String getValuePattern() {
        return valuePattern;
    }

    public void setInitializer(String targetClassName) {
        this.targetClassName = targetClassName;
        try {
            targetClass = Class.forName(targetClassName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MapEditorComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getInitializer() {
        return targetClassName;
    }
}
