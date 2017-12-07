/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances.filter;

import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.interfaces.FilterModel;
import java.time.LocalDate;

/**
 *
 * @author dkobuladze
 */
public class BalanceFilterModel extends FilterModel {

    private static final String PREF_CURRENCY_KEY = "report/filter/currency";
    
    private LocalDate date; // Local date does not save into preferences, so class use private instance for return selected localDate from dialog.
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCurrency(Currency currency) {
        saveIntoPref(PREF_CURRENCY_KEY, (currency == null) ? "" : currency.getIso());
    }
    
    
    public LocalDate getDate(){
        return date;
    }
    
    public boolean isSelectedConcreteCurrency(){
        return (getCurrencyIso() != null && !getCurrencyIso().equals(CurrencyComboBox.categoryALL));
    }

    public String getCurrencyIso() {
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
}
