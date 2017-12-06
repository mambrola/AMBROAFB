package ambroafb.in_outs.filter;


import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.DateConverter;
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

    private static final String PREF_FROM_DATE_KEY = "inout/filter/from_date";
    private static final String PREF_TO_DATE_KEY = "inout/filter/to_date";
    private static final String PREF_CURRENCY_KEY = "inout/filter/currency";
    
    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_FROM_DATE_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_TO_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setCurrency(Currency currency) {
        saveIntoPref(PREF_CURRENCY_KEY, (currency == null) ? "" : currency.getIso());
    }
    
    
    @Override
    public LocalDate getFromDate() {
        String date = getStringFromPref(PREF_FROM_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }

    @Override
    public LocalDate getToDate() {
        String date = getStringFromPref(PREF_TO_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public boolean isSelectedConcreteCurrency(){
        return (getCurrencyIso() != null && !getCurrencyIso().equals(CurrencyComboBox.categoryALL));
    }

    public String getCurrencyIso() {
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
    
}
