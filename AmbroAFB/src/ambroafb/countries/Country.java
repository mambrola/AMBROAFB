/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author mambroladze
 */
public class Country extends EditorPanelable{

    @JsonProperty("countryCode")
    @AView.Column(width = "24")
    private final StringProperty code = new SimpleStringProperty();

    @JsonProperty("descrip")
    @AView.Column(title = "%descrip", width = "250")
    private final StringProperty name = new SimpleStringProperty();

    @JsonIgnore
    private final StringExpression description;

    public Country() {
        description = code.concat("\t").concat(name);
    }

//    @Override
//    public String toString(){
//        return getCode().concat("\t").concat(getName());
//    }
    
    
    public Country(String code, String name) {
        this();
        this.code.set(code);
        this.name.set(name);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String value) {
        code.set(value);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public static Country getOneFromDB(String countryCode) throws IOException, KFZClient.KFZServerException {
        String country_json = GeneralConfig.getInstance().getServerClient().get("countries/" + countryCode);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(country_json, Country.class);
    }

    public static List<Country> getAllFromDB() {
        String countries_json;
        try {
            countries_json = GeneralConfig.getInstance().getServerClient().get("countries");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(countries_json, new TypeReference<ArrayList<Country>>(){});
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Country.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public boolean equals(Country other){
        return this.code.get().equals(other.getCode()) && this.name.get().equals(other.getName());
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
        clone.recId = recId;
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable object) {
        Country other = (Country) object;
        setCode(other.getCode());
        setName(other.getName());
    }
}
