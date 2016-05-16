/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.country_combobox;

import ambroafb.AmbroAFB;
import ambroafb.countries.Country;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author dato
 */
public class CountryComboBox extends ComboBox<Country> {
    
    public CountryComboBox(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/save_button/CountryComboBox.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot((ComboBox)this);
        loader.setController((ComboBox)this);
        try {
            loader.load();
        } catch (IOException exception) { throw new RuntimeException(exception); }
        
        asignCollection();
        
    }
        
        
        
        

    public void selectItem(Country country){
        this.getSelectionModel().select(country);
    }

    private void asignCollection() {
        this.getItems().clear();
        this.getItems().addAll(Country.getAllFromDB());
    }
}
