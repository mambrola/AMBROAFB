/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.integer_slider;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

/**
 *
 * @author dkobuladze
 */
public class IntegerSlider extends Slider {
    
    private final IntegerProperty currentValue = new SimpleIntegerProperty();
    
    private int blockValue = 1; // by default
    
    public IntegerSlider(){
//        slider.setMin(sliderMin);
//        slider.setMax(sliderMax);
//        slider.setValue(3 * blockValue);
//        setMajorTickUnit(blockValue);
        setMinorTickCount(0);
        setShowTickMarks(true);
        setSnapToTicks(true);
        valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            setValue((newValue.intValue() / blockValue) * blockValue);
        });
        
        currentValue.bind(Bindings.createIntegerBinding(() -> (int)(this.getValue() / blockValue), this.valueProperty()));
    }
    
    public void setRange(int min, int max, int counter){
        setMin(min);
        setMax(max);
        blockValue = counter;
        setMajorTickUnit(blockValue);
    }
    
    public void setDefault(int value){
        setValue(value);
    }
    
    public IntegerProperty currnetValueProperty(){
        return currentValue;
    }
}
