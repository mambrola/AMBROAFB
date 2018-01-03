/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.general.DateCellFactory;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {
    
    @AView.Column(title = "%date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_CENTER, cellFactory = DateCellFactory.LocalDateCell.class)
    private final ObjectProperty<LocalDate> date;
    
    @AView.Column(title = "%count", width = "50", styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty count;
    
    @AView.Column(title = "%iso", width = TableColumnFeatures.Width.ISO, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty iso;
    
    @AView.Column(title = "%rate", width = "80", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty rate;
    
    public CurrencyRate(){
        date = new SimpleObjectProperty<>();
        count = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        rate = new SimpleStringProperty("");
        
    }
    
    
    // Properties:
    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }
    
    public StringProperty countProperty(){
        return count;
    }
    
    public StringProperty isoProperty() {
        return iso;
    }
    
    public StringProperty rateProperty() {
        return rate;
    }
    
    
    // Getters:
    @JsonIgnore
    public String getDateColumn(){
        return (date.get() == null) ? null : date.get().toString();
    }
    
    public String getDate() {
        return (date.get() == null) ? null : date.get().toString();
    }
    
    public Integer getCount(){
        return NumberConverter.stringToInteger(count.get(), null);
    }
    
    public String getIso(){
        return iso.get();
    }
            
    public Double getRate(){
        return NumberConverter.stringToDouble(rate.get(), null);
    }
    
    // Setters:
    public void setDate(String date) {
        this.date.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setCount(Integer count){
        this.count.set((count == null) ? null : count.toString());
    }
    
    public void setIso(String iso) {
        this.iso.set(iso);
    }
    
    public void setRate(Double rate){
        this.rate.set((rate == null) ? null : NumberConverter.convertNumberToStringBySpecificFraction(rate, 4));
    }
    
    
    @Override
    public CurrencyRate cloneWithoutID() {
        CurrencyRate clone = new CurrencyRate();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public CurrencyRate cloneWithID() {
        CurrencyRate clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        CurrencyRate source = (CurrencyRate) other;
        setDate(source.getDate());
        setCount(source.getCount());
        setIso(source.getIso());
        setRate(source.getRate());
    }

    @Override
    public String toStringForSearch() {
        return getDate();
    }
    
    /**
     * Method compares two CurrencyRates.
     * @param backup Other currencyRate
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    @Override
    public boolean compares(EditorPanelable backup) {
        CurrencyRate currencyRateBackup = (CurrencyRate) backup;
        return  Objects.equals(getDate(), currencyRateBackup.getDate()) &&
                Objects.equals(getCount(), currencyRateBackup.getCount())    &&
                Objects.equals(getIso(), currencyRateBackup.getIso())   &&
                Objects.equals(getRate(), currencyRateBackup.getRate());
    }
    
    /**
     * The method compares two CurrencyRates.
     * @param other Other object, that is not null.
     * @return 
     * @see ambroafb.general.interfaces.EditorPanelable#compareById(ambroafb.general.interfaces.EditorPanelable)  EditorPanelable method "compareById"
     */
    public int compareTo(CurrencyRate other){
        int result = other.dateProperty().get().compareTo(dateProperty().get());
        if (result == 0) 
            result = getIso().compareTo(other.getIso());
        return result;
    }
}
