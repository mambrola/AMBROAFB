/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.countcombobox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class CountComboBoxItem extends HBox {

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
    
    public CountComboBoxItem(String stringForItemLabel){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ambroafb/products/countcombobox/CountComboBoxItem.fxml"));
        assignLoader(loader);
        
        itemName.setText(stringForItemLabel);
        numberLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            return "" + numberProperty.get();
        }, numberProperty));
        
        up.setOnMousePressed((MouseEvent event) -> {
            if (numberProperty.get() < upDelimiter)
                numberProperty.set(numberProperty.get() + 1);
        });
        down.setOnMousePressed((MouseEvent event) -> {
            if (numberProperty.get() > downDelimiter)
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
    }
    
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(CountComboBoxItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void setItemName(String name){
        itemNameProperty.set(name);
    }
    
    public String getItemName(){
        return itemNameProperty.get();
    }
    
    public IntegerProperty itemNumberProperty(){
        return numberProperty;
    }
    
    public StringExpression itemNameExpression(){
        return nameExpression;
    }
}
