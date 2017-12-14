/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambro.AView;
import ambroafb.general.DateCellFactory;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
@SuppressWarnings("EqualsAndHashcode")
public class Currency extends EditorPanelable {

    
    @AView.Column(title = "%date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_CENTER, cellFactory = DateCellFactory.LocalDateCell.class)
    private final ObjectProperty<LocalDate> createdDate;
    
    @AView.Column(title = "%currency_name", width = TableColumnFeatures.Width.ISO, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty iso;
    
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty descrip;
    
    @AView.Column(width = "20")
    private final StringProperty symbol;
    
    public Currency(){
        createdDate = new SimpleObjectProperty<>();
        iso = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        symbol = new SimpleStringProperty("");
        
    }

    
    // Properties:
    public ReadOnlyObjectProperty<LocalDate> createdDateProperty(){
        return createdDate;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty symbolProperty(){
        return symbol;
    }
    
    
    // Getters:
    @JsonIgnore
    public String getCreatedDate(){
        return (createdDate.get() == null) ? null : createdDate.get().toString();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public String getSymbol(){
        return symbol.get();
    }
    
    
    // Setters:
    @JsonProperty
    private void setCreatedDate(String date) {
        this.createdDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setSymbol(String symbol){
        this.symbol.set(symbol);
    }
    
    
    @Override
    public Currency cloneWithoutID() {
        Currency clone = new Currency();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Currency cloneWithID() {
        Currency clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Currency otherCurrency = (Currency) other;
        setIso(otherCurrency.getIso());
        setDescrip(otherCurrency.getDescrip());
        setSymbol(otherCurrency.getSymbol());
        setCreatedDate(otherCurrency.getCreatedDate());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
    @Override
    public String toString(){
        return getIso();
    }
    
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Currency otherCurrency = (Currency) other;
        return  getRecId() == otherCurrency.getRecId() ||
                getIso().equals(otherCurrency.getIso());
    }
    
    /**
     *
     * @param backup
     * @return
     */
    @Override
    public boolean compares(EditorPanelable backup){
        Currency currencyBackup = (Currency) backup;
        return  Objects.equals(getIso(), currencyBackup.getIso()) &&
                Objects.equals(getDescrip(), currencyBackup.getDescrip()) &&
                Objects.equals(getSymbol(), currencyBackup.getSymbol()) &&
                Objects.equals(getCreatedDate(), currencyBackup.getCreatedDate());
    }
    
    /**
     * The method compares two Currencies.
     * @param other Other object that is not null.
     * @return 
     * @see ambroafb.general.interfaces.EditorPanelable#compareById(ambroafb.general.interfaces.EditorPanelable)  EditorPanelable method "compareById"
     */
    public int compareByIso(Currency other){
        return getIso().compareTo(other.getIso());
    }
}
