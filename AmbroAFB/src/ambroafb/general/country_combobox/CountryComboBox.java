/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.country_combobox;

import ambroafb.AmbroAFB;
import ambroafb.countries.Country;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class CountryComboBox extends ComboBox<Country> {
    
    public CountryComboBox(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/country_combobox/CountryComboBox.fxml"));
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