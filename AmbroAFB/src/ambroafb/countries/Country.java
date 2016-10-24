/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.GeneralConfig;
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

    @AView.Column(width = "30")
    private final StringProperty code;

    @AView.Column(title = "%descrip", width = "250")
    private final StringProperty descrip;
    
    private static final String DB_TABLE_NAME = "countries";
    public static final String ALL = "ALL";
    
    public Country() {
        code = new SimpleStringProperty();
        descrip = new SimpleStringProperty("");
    }
    
    public static List<Country> getAllFromDB() {
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_TABLE_NAME, new ConditionBuilder().build()).toString();
            System.out.println("country data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Country>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static Country getOneFromDB(int recId) {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    public static Country getOneFromDB(String countryCode) {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("code", "=", countryCode).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    private static Country getOneFromDBHelper(ConditionBuilder conditionBuilder){
        try {
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
    
    // Setters:
    public void setCode(String value) {
        code.set(value);
    }

    public void setDescrip(String value){
        this.descrip.set(value);
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
    
    @Override
    public boolean compares(EditorPanelable backup){
        Country country = (Country) backup;
        return this.getCode().equals(country.getCode()) && this.getDescrip().equals(country.getDescrip());
    }

    public boolean equals(Country other){
        return getCode().equals(other.getCode());
    }
}
