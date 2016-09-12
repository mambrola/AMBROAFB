/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author mambroladze
 */
public class Country extends EditorPanelable{

    public String name; // for Client class and for old dbdatas (now is "descrip").
    
    @AView.Column(width = "30")
    private final StringProperty code;

    @AView.Column(title = "%descrip", width = "250")
    private final StringProperty descrip;
    
    private final StringProperty descrip_en;
    private final StringProperty descrip_ka;
    private final StringProperty descrip_de;

    
    public Country() {
        code = new SimpleStringProperty();
        descrip_en = new SimpleStringProperty("");
        descrip_ka = new SimpleStringProperty("");
        descrip_de = new SimpleStringProperty("");
        String lang = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
        descrip = lang.equals("ka") ? descrip_ka : (lang.equals("en")) ? descrip_en : descrip_de; 
    }
    
    public Country(String code, String descrip){ // for Client class.
        this();
        this.code.set(code);
        this.descrip_de.set(descrip);
        this.descrip_en.set(descrip);
        this.descrip_de.set(descrip);
    }

//    public static Country getOneFromDB(String countryCode) throws IOException, KFZClient.KFZServerException {
//        String country_json = GeneralConfig.getInstance().getServerClient().get("countries/" + countryCode);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(country_json, Country.class);
//    }
    public static Country getOneFromDB(int recId) throws IOException, KFZClient.KFZServerException {
        Country country = new Country();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from countries where rec_id = " + recId);
            while(set.next()){
                country.setRecId(set.getInt(1));
                country.setCode(set.getString(2));
                country.setDescrip_en(set.getString(3));
                country.setDescrip_ka(set.getString(4));
                country.setDescrip_de(set.getString(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return country;
    }

//    public static List<Country> getAllFromDB() {
//        String countries_json;
//        try {
//            countries_json = GeneralConfig.getInstance().getServerClient().get("countries");
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(countries_json, new TypeReference<ArrayList<Country>>(){});
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new ArrayList<>();
//    }
    public static List<Country> getAllFromDB() {
        ArrayList<Country> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from countries");
            while(set.next()){
                Country country = new Country();
                country.setRecId(set.getInt(1));
                country.setCode(set.getString(2));
                country.setDescrip_en(set.getString(3));
                country.setDescrip_ka(set.getString(4));
                country.setDescrip_de(set.getString(5));
                
                result.add(country);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    // Properties:
    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty descripProperty() {
        return descrip;
    }

    
    // Gettres:
    public String getCode() {
        return code.get();
    }

    public String getDescrip(){
        return descrip.get();
    }
    
    public String getDescrip_ka() {
        return descrip_ka.get();
    }
    
    public String getDescrip_en() {
        return descrip_en.get();
    }
    
    public String getDescrip_de() {
        return descrip_de.get();
    }

    
    // Setters:
    public void setCode(String value) {
        code.set(value);
    }

    public void setDescrip(String value){
        this.descrip.set(value);
    }

    public void setDescrip_ka(String value) {
        descrip_ka.set(value);
    }
    
    public void setDescrip_en(String value) {
        descrip_en.set(value);
    }
    
    public void setDescrip_de(String value) {
        descrip_de.set(value);
    }

    @Override
    public Country cloneWithoutID() {
        Country clone = new Country();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Country cloneWithID() {
        Country clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable object) {
        Country other = (Country) object;
        setCode(other.getCode());
        setDescrip(other.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return getCode().concat(getDescrip());
    }
    
    @Override
    public String toString(){
        return getCode().concat("\t").concat(getDescrip());
    }
    
    public boolean compares(Country country){
        return this.equals(country);
    }

    public boolean equals(Country other){
        return getCode().equals(other.getCode()) && getDescrip().equals(other.getDescrip());
    }
}
