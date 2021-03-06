/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.interfaces.DateFilterModel;
import java.time.LocalDate;

/**
 *
 * @author dato
 */
public class CurrencyRateFilterModel extends DateFilterModel {
    
    private static final String PREF_CURRENCY_KEY = "currency_rate/filter/currency";
    
    private LocalDate fromDate, toDate; // The dates does not save into user preferences, so using private instances.
    
    public CurrencyRateFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        fromDate = date;
    }

    public void setToDate(LocalDate date) {
        toDate = date;
    }
    
    public void setSelectedCurrency(Currency currency) {
        if (currency != null){
            saveIntoPref(PREF_CURRENCY_KEY, currency.getIso());
        }
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    @Override
    public LocalDate getFromDate(){
        return fromDate;
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    @Override
    public LocalDate getToDate(){
        return toDate;
    }
    
    public String getSelectedCurrencyIso(){
        return getStringFromPref(PREF_CURRENCY_KEY);
    }

    public boolean isSelectedConcreteCurrency() {
        String selectedIso = getStringFromPref(PREF_CURRENCY_KEY);
        return selectedIso != null && !selectedIso.equals(CurrencyComboBox.categoryALL);
    }
}
