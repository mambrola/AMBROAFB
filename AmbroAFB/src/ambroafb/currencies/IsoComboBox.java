/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class IsoComboBox extends ComboBox<String> {
    
    private final String GEL_ISO = "GEL";
    
    public IsoComboBox(){
        super();
        this.getItems().setAll(Currency.getAllIsoFromDB());
        this.setConverter(new CustomIsoConverter());
    }
    
    public void setValueToGEL(){
        FilteredList<String> list = this.getItems().filtered((elem) -> elem.equals(GEL_ISO));
        if (!list.isEmpty()){
            setValue(list.get(0));
        }
    }
    
    private class CustomIsoConverter extends StringConverter<String> {

        @Override
        public String toString(String selected) {
            return selected;
        }

        @Override
        public String fromString(String input) {
            return input.toUpperCase();
        }
        
    }
}
