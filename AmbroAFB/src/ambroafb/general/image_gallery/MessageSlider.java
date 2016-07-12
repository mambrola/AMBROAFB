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
    
    public MessageSlider(){}
    
    public MessageSlider(ObservableList<String> values, StringConverter<String> con, ResourceBundle rb) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageSlider.fxml"), rb);
        assignLoader(loader);
        this.values = values;
        msgField.textProperty().bind(Bindings.createStringBinding(() -> {
            if(values.isEmpty() || indexProperty.get() < 0 || indexProperty.get() >= values.size())
                return "";
            return con.toString(values.get(indexProperty.get()));
        }, indexProperty));
        msgField.setStyle("-fx-highlight-fill: null; -fx-highlight-text-fill: -fx-text-fill;");
        
        values.addListener((ListChangeListener.Change<? extends String> c) -> {
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
        });
        
        scrollBar.setUnitIncrement(1);
        scrollBar.setVisibleAmount(1);
        scrollBar.setBlockIncrement(1);
        scrollBar.valueProperty().bindBidirectional(indexProperty);
    }
    
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) { Logger.getLogger(MessageSlider.class.getName()).log(Level.SEVERE, null, ex); }
    }

    public void setEditable(boolean isEditable){
        msgField.setEditable(isEditable);
    }

    public DoubleProperty valueProperty(){
        return scrollBar.valueProperty();
    }

    public void setValueOn(int i) {
        scrollBar.setValue(-1);
        scrollBar.setValue(i);
    }

    public String getValue() {
        return values.get(indexProperty.get());
    }
}
