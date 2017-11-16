/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.filter;

import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.clients.Client;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.interfaces.FilterModel;

/**
 * ინახავს იმას რასაც ბაზას უგზავნის
 * @author dkobuladze
 */
public class AccountFilterModel extends FilterModel {
    
    private static final String PREF_CURRENCY_KEY = "accounts/filter/currency";
    private static final String PREF_BALACCOUNT_KEY = "accounts/filter/balaccount_id";
    private static final String PREF_CLIENT_KEY = "accounts/filter/client_id";
    private static final String PREF_TYPE_KEY = "accounts/filter/is_opened";
    private static final String PREF_TYPE_INDETERMINATE_KEY = "accounts/filter/is_opened_indeterminate";
    
    public void setCurrency(Currency currency){
        if (currency != null){
            saveIntoPref(PREF_CURRENCY_KEY, currency.getIso());
        }
    }
    
    public void setBalAccount(BalanceAccount balAcc){
        if (balAcc != null){
            saveIntoPref(PREF_BALACCOUNT_KEY, balAcc.getRecId());
        }
    }
    
    public void setClient(Client client){
        if (client != null){
            saveIntoPref(PREF_CLIENT_KEY, client.getRecId());
        }
    }
    
    public void setTypeIndeterminate(boolean isIndeterminate){
        saveIntoPref(PREF_TYPE_INDETERMINATE_KEY, isIndeterminate);
    }
    
    public void setType(boolean isOpened){
        saveIntoPref(PREF_TYPE_KEY, isOpened);
    }
    
    public boolean isSelectedConcreteCurrency(){
        return !getCurrencyIso().equals(CurrencyComboBox.categoryALL);
    }
            
    public String getCurrencyIso(){
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
    public boolean isSelectedConcreteBalAccount(){
        return getBalAccountId() > 0;
    }
    
    public int getBalAccountId(){
        return getIntFromPref(PREF_BALACCOUNT_KEY);
    }
    
    public boolean isSelectedConcreteClient(){
        return getClientId() > 0;
    }
    
    public int getClientId(){
        return getIntFromPref(PREF_CLIENT_KEY);
    }
    
    public boolean getTypeIndeterminate(){
        return getBooleanFromPref(PREF_TYPE_INDETERMINATE_KEY);
    }
    
    public boolean isTypeSelected(){
        return getBooleanFromPref(PREF_TYPE_KEY);
    }
}
