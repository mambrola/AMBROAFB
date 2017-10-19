/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CountComboBoxContainer extends HBox {

    @FXML
    private Label numberLabel, itemName, up, down;
    
    private String upFill = "▲";
    private String upTransp = "△";
    private String downFill = "▼";
    private String downTransp = "▽";
    
    private int upDelimiter = 99;
    private int downDelimiter = 0;
    
    private StringProperty itemNameProperty = new SimpleStringProperty("");
    private IntegerProperty numberProperty = new SimpleIntegerProperty(0);
    private final StringExpression nameExpression;
    private boolean isDisableState = false;
    private BiConsumer<String, Integer> numberChangeAction;
    
    public CountComboBoxContainer(CountComboBoxItem item){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ambroafb/general/countcombobox/CountComboBoxContainer.fxml"));
        assignLoader(loader);
        
        String stringForItemLabel = (item.getDrawableName() == null) ? "" : item.getDrawableName();
        itemName.setText(stringForItemLabel);
        numberLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            return "" + numberProperty.get();
        }, numberProperty));
        
        up.setOnMousePressed((MouseEvent event) -> {
            if (numberProperty.get() < upDelimiter && !isDisableState)
                numberProperty.set(numberProperty.get() + 1);
        });
        down.setOnMousePressed((MouseEvent event) -> {
            if (numberProperty.get() > downDelimiter && !isDisableState)
                numberProperty.set(numberProperty.get() - 1);
        });
        
        up.textProperty().bind(Bindings.createStringBinding(() -> {
            return (numberProperty.get() >= upDelimiter) ? upTransp : upFill;
        }, numberProperty));
        down.textProperty().bind(Bindings.createStringBinding(() -> {
            return (numberProperty.get() > downDelimiter) ? downFill : downTransp;
        }, numberProperty));
        
        nameExpression = Bindings.when (numberProperty.greaterThan(downDelimiter))
                            .then(Bindings.createStringBinding(() -> {
                                    return numberProperty.get() + "-" + stringForItemLabel;
                                }, numberProperty))
                            .otherwise("");
        
        numberProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (numberChangeAction != null){
                numberChangeAction.accept(item.getUniqueIdentifier(), newValue.intValue());
            }
        });
    }
    
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(CountComboBoxContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void increaseNumberBy(int value){
        int newValue = numberProperty.get() + value;
        if (newValue <= upDelimiter){
            numberProperty.set(newValue);
        }
    }
    
    public void decreaseNumberBy(int value){
        int newValue = numberProperty.get() - value;
        if (newValue >= 0){
            numberProperty.set(newValue);
        }
    }
    
    public void setViewableState(boolean isViewavleState){
        this.isDisableState = isViewavleState;
    }
    
    
    
    public void setItemName(String name){
        itemNameProperty.set(name);
    }
    
    public String getItemName(){
        return itemNameProperty.get();
    }
    
    public IntegerProperty numberProperty(){
        return numberProperty;
    }
    
    public StringExpression nameExpression(){
        return nameExpression;
    }
    
    public void setAction(BiConsumer<String, Integer> numberChangeAction) {
        this.numberChangeAction = numberChangeAction;
    }
}
