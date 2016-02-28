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
    
    public String getDescrip() {
        return descrip.get();
    }
    
    public static Country dbGetCountry (String countryCode){
        return dbGetCountries(countryCode).get(countryCode);
    }
    
    public static HashMap<String,Country> dbGetCountries (String countryCode){
        HashMap<String,Country> countries = new HashMap();
        String whereTest = countryCode.equals("") ? "" : " where rec_code = '" + countryCode + "'";
        try {
            Connection conn = GeneralConfig.getInstance().getConnectionToDB();
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM countries" +  whereTest + " ORDER BY rec_code");
            while (resultSet.next()) {
                String recCode = resultSet.getString("rec_code");
                String descrip = resultSet.getString("descrip");
                countries.put(recCode, new Country(recCode, descrip));
            }
        }
        catch (SQLException | NullPointerException ex) {
            Platform.runLater(() -> {
                new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
            });
        }
        System.out.println("countries: " + countries.size() + countries);
        return countries;
    }
    
}
