/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.countries.*;
import ambro.AView;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

/**
 *
 * @author mambroladze
 */
public class Country { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    @AView.Column(width = "24")
    public SimpleStringProperty countryCode;
    
    @AView.Column(title = "%descrip", width = "250")
    private SimpleStringProperty descrip;
       
    public Country(String cc, String d){
        countryCode = new SimpleStringProperty(cc);
        descrip = new SimpleStringProperty(d);
    }
    
    public static Country dbGetCountry (String countryCode){
        return dbGetCountries(countryCode).get(countryCode);
    }
    
    public static HashMap<String,Country> dbGetCountries (String countryCode){
        HashMap<String,Country> countries = new HashMap();
        String whereText = countryCode.equals("") ? "" : " where rec_code = '" + countryCode + "'";
        Utils.getArrayListsFromDB("SELECT * FROM countries" +  whereText + " ORDER BY rec_code", new String[]{"rec_code", "descrip"}).stream().forEach((row) -> {
            countries.put((String) row[0], new Country((String) row[0], (String) row[1]));
        });
        return countries;
    }
    
}
