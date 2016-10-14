/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;

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

    private static final String DB_TABLE_NAME = "countries";
    
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
        this.descrip.set(descrip);
    }

//    public static Country getOneFromDB(String countryCode) throws IOException, KFZClient.KFZServerException {
//        String country_json = GeneralConfig.getInstance().getServerClient().get("countries/" + countryCode);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.readValue(country_json, Country.class);
//    }

    public static List<Country> getAllFromDB() {
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_TABLE_NAME, new ConditionBuilder().build()).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Country>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static Country getOneFromDB(int recId) throws IOException, KFZClient.KFZServerException {
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_TABLE_NAME, conditionBuilder.build());
            
            String countryData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(countryData, Country.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        return getRecId() == other.getRecId() && getCode().equals(other.getCode()) && getDescrip().equals(other.getDescrip());
    }
}
