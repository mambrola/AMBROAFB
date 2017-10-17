/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.DateFilterModel;
import java.time.LocalDate;

/**
 *
 * @author dato
 */
public class CurrencyRateFilterModel extends DateFilterModel {
    
    private static final String PREF_BIGGER_DATE_KEY = "currency_rate/filter/dateBigger";
    private static final String PREF_LESS_DATE_KEY = "currency_rate/filter/dateLess";
    private static final String PREF_CURRENCY_KEY = "currency_rate/filter/currency";
    
    public CurrencyRateFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_BIGGER_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_LESS_DATE_KEY, (date == null) ? "" : date.toString());
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
        String date = getStringFromPref(PREF_BIGGER_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    @Override
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_LESS_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
//    public Currency getSelectedCurrency(){
//        return selectedCurrency;
//    }
    
    public String getSelectedCurrencyIso(){
        return getStringFromPref(PREF_CURRENCY_KEY);
    }

    public boolean isSelectedConcreteCurrency() {
        String selectedIso = getStringFromPref(PREF_CURRENCY_KEY);
        return selectedIso != null && !selectedIso.equals(CurrencyComboBox.categoryALL);
    }
}
