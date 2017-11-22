/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;


/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {
    
    @AView.Column(title = "%date", width = TableColumnFeatures.Width.DATE, styleClass = "textCenter")
    private final StringProperty dateForColumn;
    private final ObjectProperty<LocalDate> dateProperty;
    
    @AView.Column(title = "%count", width = "50", styleClass = "textCenter")
    private final StringProperty count;
    
    @AView.Column(title = "%iso", width = TableColumnFeatures.Width.ISO, styleClass = "textCenter")
    private final StringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%rate", width = "80", styleClass = "textRight")
    private final StringProperty rate;
    
    public CurrencyRate(){
        dateProperty = new SimpleObjectProperty<>();
        dateForColumn = new SimpleStringProperty("");
        count = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(new Currency());
        rate = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            dateForColumn.set(dateStr);
        });
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            rebindIso();
        });
        rebindIso();
    }
    
    private void rebindIso(){
        iso.unbind();
        if (currency.get() != null){
            iso.bind(currency.get().isoProperty());
        }
    }
    
    
    // Properties:
    public ObjectProperty<LocalDate> dateProperty(){
        return dateProperty;
    }
    
    public StringProperty countProperty(){
        return count;
    }
    
    public ObjectProperty<Currency> currencyProperty(){
        return currency;
    }
    
    public StringProperty rateProperty() {
        return rate;
    }
    
    
    // Getters:
    @JsonIgnore
    public String getDateColumn(){
        return dateForColumn.get();
    }
    
    public String getDate() {
        return (dateProperty.get() == null) ? "" : dateProperty.get().toString();
    }
    
    public int getCount(){
        return Utils.getIntValueFor(count.get());
    }
    
    public String getIso(){
        return currency.get().getIso();
    }
            
    public double getRate(){
        return Utils.getDoubleValueFor(rate.get());
    }
    
    // Setters:
    public void setDate(String date) {
        dateProperty.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setCount(int count){
        this.count.set("" + count);
    }
    
    public void setIso(String iso) {
        this.currency.get().setIso(iso);
    }
    
    public void setRate(double rate){
        this.rate.set("" + rate);
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
        
        return  Utils.dateEquals(dateProperty().get(), currencyRateBackup.dateProperty().get()) &&
                getCount() == currencyRateBackup.getCount()    &&
                getIso().equals(currencyRateBackup.getIso())   &&
                getRate() == currencyRateBackup.getRate();
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
