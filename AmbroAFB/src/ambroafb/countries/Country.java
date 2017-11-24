/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author mambroladze
 */
@SuppressWarnings("EqualsAndHashcode")
public class Country extends EditorPanelable{

    @AView.Column(width = "30")
    private final StringProperty code;

    @AView.Column(title = "%descrip", width = "250")
    private final StringProperty descrip;
    
    public static final String categoryALL = "ALL";
    private static final String DB_TABLE_NAME = "countries";
    private static final String REZIDENT_COUNTRY_CODE = "GE";
    
    @JsonIgnore
    private BooleanProperty rezidentCountry;
    
    public Country() {
        code = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        rezidentCountry = new SimpleBooleanProperty(false);
        
        rezidentCountry.bind(Bindings.createBooleanBinding(() -> {
            return code.get().equals(REZIDENT_COUNTRY_CODE);
        }, code));
    }
    
    // Properties:
    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty descripProperty() {
        return descrip;
    }
    
    public BooleanProperty rezidentCountryProperty(){
        return rezidentCountry;
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
        return getCode() + " " + getDescrip();
    }
    
    @Override
    public String toString(){
        return getCode().concat("\t").concat(getDescrip());
    }
    
    @Override
    public boolean compares(EditorPanelable backup){
        Country country = (Country) backup;
        return  this.getCode().equals(country.getCode()) && 
                this.getDescrip().equals(country.getDescrip());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Country otherCountry = (Country) other;
        return  getRecId() == otherCountry.getRecId() ||
                getCode().equals(otherCountry.getCode());
    }

    /**
     *  The method compares two countries by description.
     * @param other Other object that is not null.
     * @return
     * @see ambroafb.general.interfaces.EditorPanelable#compareById(ambroafb.general.interfaces.EditorPanelable)  EditorPanelable method "compareById"
     */
    public int compareByDescrip(Country other){
        return getDescrip().compareTo(other.getDescrip());
    }
}
