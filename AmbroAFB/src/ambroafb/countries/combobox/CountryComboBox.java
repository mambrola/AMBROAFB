/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries.combobox;

import ambroafb.AmbroAFB;
import ambroafb.countries.Country;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class CountryComboBox extends ComboBox<Country> {
    
    public CountryComboBox(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/countries/combobox/CountryComboBox.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot((ComboBox)this);
        loader.setController((ComboBox)this);
        try {
            loader.load();
        } catch (IOException exception) { throw new RuntimeException(exception); }

        this.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCode() + "\t" + country.getName();
            }
            @Override
            public Country fromString(String string) {
                return null;
            }
        });
    }
    
    public void selectItem(Country country){
        this.getSelectionModel().select(country);
    }

}