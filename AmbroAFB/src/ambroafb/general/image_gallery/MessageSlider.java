					/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.Utils;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 * 
 * Does not use Utils.createScene("path/MessageSlider.fxml", (Object)this); 
 * because this class must not extend VBox, but it is needed, 
 * that we can add MessageSlider as scene object.
 */
public class MessageSlider extends VBox {
    
    @FXML
    private TextField msgField;
    @FXML
    private ScrollBar scrollBar;

    private DoubleProperty indexProperty = new SimpleDoubleProperty(-1.0);
    private ObservableList<String> values;
    private StringConverter<String> converter;
    
    public MessageSlider(ObservableList<String> values, StringConverter<String> con, ResourceBundle rb) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageSlider.fxml"), rb);
        assignLoader(loader);
        this.values = values;
        converter = con;
        msgField.textProperty().bind(Bindings.createStringBinding(() -> {
            if(invalidState())
                return "";
            String value = values.get(indexProperty.getValue().intValue());
            return (converter == null) ? value : converter.toString(value);
        }, indexProperty));
        // deselect value in textField:
        msgField.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: -fx-text-fill;");
        
        values.addListener(new ListActionListener());
        
        scrollBar.setUnitIncrement(1);
        scrollBar.setVisibleAmount(1);
        scrollBar.setBlockIncrement(1);
        
        scrollBar.valueProperty().bindBidirectional(indexProperty);
        scrollBar.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            scrollBar.setValue(Math.round(newValue.doubleValue()));
        });
    }
    
    /**
     * The method assigns loader constructor and root.
     * @param loader 
     */
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) { Logger.getLogger(MessageSlider.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    /**
     * The method checks index range and values list edge cases.
     * @return - True - if index is out of bounds or values list is null or empty.
     */
    private boolean invalidState(){
        return values == null || values.isEmpty() || indexProperty.getValue().intValue() < 0 || indexProperty.getValue().intValue() >= values.size();
    }

    /**
     * The method provides to become editable a textField.
     * @param isEditable - True - if user want to enter text in textField, false - otherwise.
     */
    public void setEditable(boolean isEditable){
        msgField.setEditable(isEditable);
    }

    /**
     * @return - ScrollBar indexProperty.
     */
    public DoubleProperty indexProperty(){
        return indexProperty;
    }

    /**
     * The method returns value on given index.
     * @param index - If index is out of bounds, method sets zero indexed value.
     */
    public void setValueOn(int index) {
        if (values == null) return;
        if (index < 0 || index >= values.size()){
            index = 0;
        }
        indexProperty.set(index);
    }

    /**
     * The method return current value.
     * @return - If values list is null or empty or index is out of bounds, 
     * method returns empty string. Otherwise it returns value according to stringConverter.
     */
    public String getValue() {
        String value = "";
        if (!invalidState()){
            value = values.get(indexProperty.getValue().intValue());
        }
        return value;
    }
    
    /**
     * The inner class provides to listen any manipulation 
     * on observable list (we listen only remove actions, 
     * because add action will cause download image permanently) and make appropriate logic.
     */
    private class ListActionListener implements ListChangeListener<String> {

        @Override
        public void onChanged(Change<? extends String> c) {
            while(c.next()){
                if(c.wasRemoved()){
                    if (indexProperty.getValue().intValue() == values.size()){
                        indexProperty.set(indexProperty.getValue().intValue() - 1);
                    }
                }
                if(values.size() > 0){
                    scrollBar.setMax(values.size() - 1);
                    scrollBar.setVisibleAmount(((double)values.size() - 1)/values.size());
                }
            }
        }
        
    }
}
