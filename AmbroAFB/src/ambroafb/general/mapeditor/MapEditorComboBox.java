/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.mapeditor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
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
    public class MapEditorComboBox extends ComboBox<Element> {

    private final Map<String, String> elements = new HashMap<>();
    private String delimiter = " : ";
    private String keyPattern, valuePattern;
    private final SortedList sortedList;
    private final ObservableList<Element> comboBoxItemsList;
    private final MapEditorComboBox mapEditorComboBox;
    
    public MapEditorComboBox() {
        mapEditorComboBox = (MapEditorComboBox)this;
        this.setEditable(true);
        TextField editor = getEditor();
        editor.setText(delimiter);
        this.setConverter(new CustomStringConverter());
        
        comboBoxItemsList = getItems();
        // create SortedList with target list and Comparator logic:
        sortedList = new SortedList<>(comboBoxItemsList, (Element elem1, Element elem2) -> {
            int key1 = Integer.parseInt(elem1.getKey());
            int key2 = Integer.parseInt(elem2.getKey()); 
            return key1 - key2;
        });
        this.setItems(sortedList);
        
        // Control textField input.
        editor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.isEmpty()) editor.setText(delimiter);
            else {
                if (!newValue.contains(delimiter)) {
                    editor.setText(oldValue);
                }
                else {
                    String keyInput = StringUtils.substringBefore(newValue, delimiter).trim();
                    String valueInput = StringUtils.substringAfter(newValue, delimiter).trim();
                    if (!Pattern.matches(keyPattern, keyInput)){
                        keyInput = StringUtils.substringBefore(oldValue, delimiter).trim();
                    }
                    if (!Pattern.matches(valuePattern, valueInput)){
                        valueInput = StringUtils.substringAfter(oldValue, delimiter).trim();
                    }
                    editor.setText(keyInput + delimiter + valueInput);
                }
            }
        });

        // Control caret position in textField.
        editor.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            int caretOldPos = editor.getCaretPosition();
            int delimiterIndex = editor.getText().indexOf(delimiter);
            if (event.getCode().equals(KeyCode.RIGHT)){
                if (caretOldPos + 1 > delimiterIndex && caretOldPos + 1 <= delimiterIndex + delimiter.length()){
                    editor.positionCaret(delimiterIndex + delimiter.length());
                    event.consume();
                }
            }
            else if (event.getCode().equals(KeyCode.LEFT)){
                if (caretOldPos - 1 >= delimiterIndex && caretOldPos - 1 < delimiterIndex + delimiter.length()){
                    editor.positionCaret(delimiterIndex);
                    event.consume();
                }
            }
        });
        
    }
    
    
    // Setters and getters for pattern. The also use from fxml.
    
    public void setKeyPattern(String keyPattern){
        this.keyPattern = keyPattern;
    }

    public String getKeyPattern(){
        return keyPattern;
    }

    public String getValuePattern() {
        return valuePattern;
    }

    public void setValuePattern(String valuePattern) {
        this.valuePattern = valuePattern;
    }


    /**
     * The custom StringConverter provides to create Element class type object from ComboBox editor
     * and create items showing string for listView.
     * The logic is following:
     * If item not selected, then new item adds if this key value is unique.
     * If item is selected, then it updates if this key value is unique.
     * Because of setValue(null) command, editor text becomes separator (textProperty listener pay attention this case), 
     * so we not add it in items.
     */
    private class CustomStringConverter extends StringConverter<Element> {

        @Override
        public String toString(Element elem) {
            return (elem == null) ? delimiter : elem.toString();
        }

        @Override
        public Element fromString(String input) {
            String key = StringUtils.substringBefore(input, delimiter).trim();
            String value = StringUtils.substringAfter(input, delimiter).trim();
            int selectedIndex = mapEditorComboBox.getSelectionModel().getSelectedIndex();
            Element selectedItem = mapEditorComboBox.getSelectionModel().getSelectedItem();
            if (selectedIndex < 0) { // If nothing is selected then user want to add new item.
                selectedItem = new Element(key, value);
                if (!elements.containsKey(key) && !input.equals(delimiter) && !key.isEmpty() && !value.isEmpty()) {
                    comboBoxItemsList.add(selectedItem);
                    elements.put(key, value);
                }
            } else {
                if (input.equals(delimiter)){ // If remove selected item, then remove from structures.
                    elements.remove(selectedItem.getKey());
                    comboBoxItemsList.remove(selectedIndex);
                    mapEditorComboBox.setValue(null);
                }
                else if (key.isEmpty() || value.isEmpty()){ // If key or value is not fill.
                    selectedItem = new Element(key, value); // Don't disappare input.
                }
                else { // If user wants to update item.
                    if (!key.equals(selectedItem.getKey()) && !elements.containsKey(key)) {
                        elements.remove(selectedItem.getKey()); // remove old key from map
                        selectedItem.setKey(key);
                    }
                    selectedItem.setValue(value);
                    comboBoxItemsList.remove(selectedItem);
                    comboBoxItemsList.add(selectedItem);
                    elements.put(key, value);
                    mapEditorComboBox.setValue(null); // disappare input in textField and deselect item value/index.
                }
            }
            return selectedItem;
        }
    }
    
}
