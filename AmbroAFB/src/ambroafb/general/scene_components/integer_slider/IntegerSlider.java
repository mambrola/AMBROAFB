/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.integer_slider;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Slider;

/**
 *
 * @author dkobuladze
 */
public class IntegerSlider extends Slider {
    
    private final IntegerProperty currentValue = new SimpleIntegerProperty();
    
    public IntegerSlider(){
        
    }
    
    public void setRange(int min, int max){
        setMin(min);
        setMax(max);
    }
    
    public void setDefault(int value){
        
    }
    
    public IntegerProperty currnetValueProperty(){
        return currentValue;
    }
}
