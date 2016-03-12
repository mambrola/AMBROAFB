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
public class Country { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითონ tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    @AView.Column(width = "24")
    public String countryCode;
    
    @AView.Column(title = "%descrip", width = "250")
    private String descrip;
       
    public Country(Object[] values){
        countryCode = (String) values[0];
        descrip     = (String) values[1];
    }
    
    public String getDescrip(){
        return descrip;
    }
    
    public String getFullDescrip(){
        return countryCode + "\t" + descrip;
    }
    
    public static Country dbGetCountry (String countryCode){
        return dbGetCountries(countryCode).get(countryCode);
    }
    
    public static HashMap<String,Country> dbGetCountries (String recCode){
        HashMap<String,Country> countries = new HashMap();
        String query = "SELECT * FROM countries" + ("".equals(recCode) ? "" : " where rec_id = '" + recCode + "'") + " ORDER BY rec_code";
        String[] orderedRequestedFields = new String[]{"rec_code", "descrip"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {
            countries.put((String) row[0], new Country(row));
        });
        return countries;
    }
    
}
