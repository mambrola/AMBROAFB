/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class IsoComboBox extends ComboBox<String> {
    
    public IsoComboBox(){
        super();
        this.getItems().setAll(Currency.getAllIsoFromDB());
        this.setConverter(new CustomIsoConverter());
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
