/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances.filter;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.FilterModel;
import java.time.LocalDate;

/**
 *
 * @author dkobuladze
 */
public class BalanceFilterModel extends FilterModel {

    private static final String PREF_DATE_KEY = "report/filter/date";
    private static final String PREF_CURRENCY_KEY = "report/filter/currency";
    
    public void setDate(LocalDate date) {
        saveIntoPref(PREF_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setCurrency(Currency currency) {
        saveIntoPref(PREF_CURRENCY_KEY, (currency == null) ? "" : currency.getIso());
    }
    
    
    public LocalDate getDate(){
        String date = getStringFromPref(PREF_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public boolean isSelectedConcreteCurrency(){
        return (getCurrencyIso() != null && !getCurrencyIso().equals(CurrencyComboBox.categoryALL));
    }

    public String getCurrencyIso() {
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
}
