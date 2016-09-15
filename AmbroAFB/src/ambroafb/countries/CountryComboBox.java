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
    
    public final Country all = new Country();
    
    public CountryComboBox(){
        all.setRecId(0);
        all.setCode("ALL");
        all.setDescrip("ALL Country");

        this.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCode();
            }
            @Override
            public Country fromString(String string) {
                return null;
            }
        });
        this.getItems().addAll(Country.getAllFromDB());
        this.getItems().add(0, all);
    }
    
    public void selectItem(Country country){
        this.getSelectionModel().select(country);
    }

    public void showCategoryAll(boolean show){
        if (!show){
            if (getItems().contains(all)){
                getItems().remove(0);
            }
        }
        else {
            if (!getItems().contains(all)){
                getItems().add(all);
            }
        }
    }
}