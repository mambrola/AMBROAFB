/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class CountryComboBox extends ComboBox<Country> {
    
    public CountryComboBox(){

        this.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCode() + "\t" + country.getDescrip();
            }
            @Override
            public Country fromString(String string) {
                return null;
            }
        });
        this.getItems().addAll(Country.getAllFromDB());
    }
    
    public void selectItem(Country country){
        this.getSelectionModel().select(country);
    }

}