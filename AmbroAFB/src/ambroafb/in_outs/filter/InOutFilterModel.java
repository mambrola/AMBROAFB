package ambroafb.in_outs.filter;


import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.interfaces.DateFilterModel;
import java.time.LocalDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dkobuladze
 */
public class InOutFilterModel extends DateFilterModel {

    private static final String PREF_CURRENCY_KEY = "inout/filter/currency";
    
    private LocalDate fromDate, toDate; // LocalDates do not save into preferences, so private instances use to return selected localDates values from dialog.
    
    public void setFromDate(LocalDate date) {
        fromDate = date;
    }
    
    public void setToDate(LocalDate date) {
        toDate = date;
    }

    public void setCurrency(Currency currency) {
        saveIntoPref(PREF_CURRENCY_KEY, (currency == null) ? "" : currency.getIso());
    }
    
    
    @Override
    public LocalDate getFromDate() {
        return fromDate;
    }

    @Override
    public LocalDate getToDate() {
        return toDate;
    }
    
    public boolean isSelectedConcreteCurrency(){
        return (getCurrencyIso() != null && !getCurrencyIso().equals(CurrencyComboBox.categoryALL));
    }

    public String getCurrencyIso() {
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
    
}
