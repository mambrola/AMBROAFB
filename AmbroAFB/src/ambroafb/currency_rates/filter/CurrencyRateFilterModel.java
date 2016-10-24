/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates.filter;

import ambroafb.currencies.Currency;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import java.time.LocalDate;

/**
 *
 * @author dato
 */
public class CurrencyRateFilterModel extends FilterModel {
    
    private static final String DATE_BIGGER = "1970-01-01";
    private static final String DATE_LESS = "9999-01-01";
    
    private static final String PREF_BIGGER_DATE_KEY = "currency_rate/filter/dateBigger";
    private static final String PREF_LESS_DATE_KEY = "currency_rate/filter/dateLess";
    private static final String PREF_CURRENCY_ISO_KEY = "currency_rate/filter/currencyIso";
    
    private Currency selectedCurrency;
            
    public CurrencyRateFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_BIGGER_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_LESS_DATE_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setSelectedCurrencyIndex(int index){
        saveIntoPref(PREF_CURRENCY_ISO_KEY, index);
    }

    public void setSelectedCurrency(Currency currency) {
        selectedCurrency = currency;
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    public LocalDate getFromDate(){
        String date = getStringFromPref(PREF_BIGGER_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is min date;
     * @return 
     */
    public LocalDate getFromDateForDB(){
        LocalDate fromDate = getFromDate();
        if (fromDate == null){
            fromDate = DateConverter.getInstance().parseDate(DATE_BIGGER);
        }
        return fromDate;
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_LESS_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is max date;
     * @return 
     */
    public LocalDate getToDateForDB(){
        LocalDate toDate = getToDate();
        if (toDate == null){
            toDate = DateConverter.getInstance().parseDate(DATE_LESS);
        }
        return toDate;
    }
    
    public int getSelectedCurrencyIndex(){
        return getIntFromPref(PREF_CURRENCY_ISO_KEY);
    }
    
    public Currency getSelectedCurrency(){
        return selectedCurrency;
    }

    public boolean isSelectedCurrencyALL() {
        return (selectedCurrency != null && selectedCurrency.getRecId() < 1);
    }
}
