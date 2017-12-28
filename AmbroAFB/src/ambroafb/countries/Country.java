/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambro.AView;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author mambroladze
 */
@SuppressWarnings("EqualsAndHashcode")
public class Country extends EditorPanelable{

    public static final String categoryALL = "ALL";
    private static final String DB_TABLE_NAME = "countries";
    private static final String REZIDENT_COUNTRY_CODE = "GE";
    
    @AView.Column(width = "30")
    private final StringProperty code;

    @AView.Column(title = "%descrip", width = "250")
    private final StringProperty descrip;
    
    @AView.Column(title = "%capital", width = "100")
    private final StringProperty capital;
    
    @AView.Column(title = "%prefix_phone", width = "60")
    private final StringProperty phonePrefix;
    
    @AView.Column(width = "60", cellFactory = FlagCellFactory.class)
    private final StringProperty flagSUrl;
    
    private String code3, flagUrl;
    
    @JsonIgnore
    private BooleanProperty rezidentCountry;

    
    public Country() {
        code = new SimpleStringProperty(); // Default value is need for binding to rezidentCountry.
        descrip = new SimpleStringProperty();
        capital = new SimpleStringProperty();
        phonePrefix = new SimpleStringProperty();
        flagSUrl = new SimpleStringProperty();
        rezidentCountry = new SimpleBooleanProperty(false);
        
        
        rezidentCountry.bind(Bindings.createBooleanBinding(() -> {
            if (code.get() == null) return false;
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
    
    public StringProperty capitalProperty() {
        return capital;
    }
    
    public StringProperty phonePrefixProperty() {
        return phonePrefix;
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

    public String getCapital() {
        return capital.get();
    }

    public String getPhonePrefix() {
        return phonePrefix.get();
    }

    public String getCode3() {
        return code3;
    }

    public String getFlagSUrl() {
        return flagSUrl.get();
    }

    public String getFlagUrl() {
        return flagUrl;
    }
    
    
    
    // Setters:
    public void setCode(String value) {
        code.set(value);
    }

    public void setDescrip(String value){
        this.descrip.set(value);
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix.set(phonePrefix);
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public void setCapital(String capital) {
        this.capital.set(capital);
    }

    public void setFlagSUrl(String flagSUrl) {
        this.flagSUrl.set(flagSUrl);
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
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
        String res = getCapital() + " " + getCode() + " " + getDescrip() + " " + getPhonePrefix();
        System.out.println("res: " + res);
        return  getCode() + " " + getDescrip() + " " + getCapital() + " " + getPhonePrefix();
    }
    
    @Override
    public String toString(){
        return getCode().concat("\t").concat(getDescrip());
    }
    
    @Override
    public boolean compares(EditorPanelable backup){
        Country country = (Country) backup;
        return  Objects.equals(getCode(), country.getCode()) && 
                Objects.equals(getDescrip(), country.getDescrip()) &&
                Objects.equals(getPhonePrefix(), country.getPhonePrefix()) &&
                Objects.equals(getCode3(), country.getCode3()) &&
                Objects.equals(getCapital(), country.getCapital()) &&
                Objects.equals(getFlagSUrl(), country.getFlagSUrl()) &&
                Objects.equals(getFlagUrl(), country.getFlagUrl());
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Country otherCountry = (Country) other;
        return  getRecId() == otherCountry.getRecId() || compares(otherCountry);
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
    
    
    public static class FlagCellFactory implements Callback<TableColumn<Country, String>, TableCell<Country, String>> {

        @Override
        public TableCell<Country, String> call(TableColumn<Country, String> param) {
            return new TableCell<Country, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){
                        setGraphic(null);
                    }
                    else {
                        System.out.println("Download Flags Here ...");
//                        new Thread(() -> {
//                            Platform.runLater(() -> {});
//                        }).start();
                    }
                }
            };
        }
        
    }
}
