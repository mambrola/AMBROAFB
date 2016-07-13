					/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class MessageSlider extends VBox {
    
    @FXML
    private TextField msgField;
    @FXML
    private ScrollBar scrollBar;

    private IntegerProperty indexProperty = new SimpleIntegerProperty(-1);
    private ObservableList<String> values;
    
    public MessageSlider(ObservableList<String> values, StringConverter<String> con, ResourceBundle rb) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageSlider.fxml"), rb);
        assignLoader(loader);
        this.values = values;
        msgField.textProperty().bind(Bindings.createStringBinding(() -> {
            if(invalidState())
                return "";
            String value = values.get(indexProperty.get());
            return (con == null) ? value : con.toString(value);
        }, indexProperty));
        // deselect value in textField:
        msgField.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: -fx-text-fill;");
        
        values.addListener(new ListActionListener());
        
        scrollBar.setUnitIncrement(1);
        scrollBar.setVisibleAmount(1);
        scrollBar.setBlockIncrement(1);
        scrollBar.valueProperty().bindBidirectional(indexProperty);
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
        return values == null || values.isEmpty() || indexProperty.get() < 0 || indexProperty.get() >= values.size();
    }

    /**
     * The method provides to become editable a textField.
     * @param isEditable - True - if user want to enter text in textField, false - otherwise.
     */
    public void setEditable(boolean isEditable){
        msgField.setEditable(isEditable);
    }

    /**
     * @return - ScrollBar valueProperty.
     */
    public DoubleProperty valueProperty(){
        return scrollBar.valueProperty();
    }

    /**
     * The method returns value on given index.
     * @param index - If index is out of bounds, method sets zero indexed value.
     */
    public void setValueOn(int index) {
        if (values == null) return;
        scrollBar.setValue(-1);
        if (index < 0 || index >= values.size()){
            index = 0;
        }
        scrollBar.setValue(index);
    }

    /**
     * The method return current value.
     * @return - If values list is null or empty or index is out of bounds, 
     * method returns empty string.
     */
    public String getValue() {
        String value = "";
        if (!invalidState()){
            value = values.get(indexProperty.get());
        }
        return value;
    }
    
    /**
     * The inner class provides to listen any manipulation 
     * on observable list (we listen only add and remove actions) and make appropriate logic.
     */
    private class ListActionListener implements ListChangeListener<String> {

        @Override
        public void onChanged(Change<? extends String> c) {
            while(c.next()){
                if (c.wasAdded()){
                    indexProperty.set(values.size() - 1);
                }
                else if(c.wasRemoved()){
                    if (indexProperty.get() == values.size()){
                        indexProperty.set(indexProperty.get() - 1);
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
