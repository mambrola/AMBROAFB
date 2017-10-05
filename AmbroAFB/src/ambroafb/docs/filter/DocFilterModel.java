/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.filter;

import ambroafb.accounts.Account;
import ambroafb.currencies.Currency;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.docs.DocCode;
import ambroafb.docs.DocCodeComboBox;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.FilterModel;
import java.time.LocalDate;

/**
 * Logic: Save scene components values which are needed for sending to DB.
 * @author dkobuladze
 */
public class DocFilterModel extends FilterModel {

    private final String PREF_DOC_DATE_FROM_KEY = "docs/filter/doc_date_from";
    private final String PREF_DOC_DATE_TO_KEY = "docs/filter/doc_date_to";
    private final String PREF_DOC_IN_DOC_DATE_FROM_KEY = "docs/filter/doc_in_doc_date_from";
    private final String PREF_DOC_IN_DOC_DATE_TO_KEY = "docs/filter/doc_in_doc_date_to";
    private final String PREF_ACCOUNT_ID_KEY = "docs/filter/selected_account_id";
    private final String PREF_CURRENCY_KEY = "docs/filter/currency";
    private final String PREF_DOC_CODE_KEY = "docs/filter/doc_code";
    
    public DocFilterModel(){
        
    }
    
    /**
     * The method saves docDate datePicker value.
     * @param isFromDate True if value is DateFrom component. False if value is DateTo component.
     * @param date The value of datePicker.
     */
    public void setDocDate(boolean isFromDate, LocalDate date){
        String key = (isFromDate) ? PREF_DOC_DATE_FROM_KEY : PREF_DOC_DATE_TO_KEY;
        saveDateForKey(key, date);
    }
    
    /**
     * The method saves docInDocDate datePicker value.
     * @param isFromDate True if value is DateFrom component. False if value is DateTo component.
     * @param date The value of datePicker.
     */
    public void setDocInDocDate(boolean isFromDate, LocalDate date){
        String key = (isFromDate) ? PREF_DOC_IN_DOC_DATE_FROM_KEY : PREF_DOC_IN_DOC_DATE_TO_KEY;
        saveDateForKey(key, date);
    }
    
    private void saveDateForKey(String key, LocalDate date){
        saveIntoPref(key, (date == null) ? "" : date.toString());
    }
    
    /**
     * The method saves account id if account is not null.
     * @param account The account that is chosen from filter dialog.
     */
    public void setSelectedAccount(Account account){
        if (account != null){
            saveIntoPref(PREF_ACCOUNT_ID_KEY, account.getRecId());
        }
    }
    
    /**
     * The method saves currency iso if currency is not null.
     * @param currency The currency that is chosen from filter dialog.
     */
    public void setSelectedCurrency(Currency currency){
        if (currency != null){
            saveIntoPref(PREF_CURRENCY_KEY, currency.getIso());
        }
    }
    
    /**
     * The method saves docCode description if docCode is not null.
     * @param docCode The docCode that is chosen from filter dialog.
     */
    public void setSelectedDocCode(DocCode docCode){
        if (docCode != null){
            saveIntoPref(PREF_DOC_CODE_KEY, docCode.getDocCode());
        }
    }
    
    
    /**
     *  The method gets saving docDate value.
     * @param isFromDate True if interests dateFrom value. False if interests dateTo value.
     * @return 
     */
    public LocalDate getDocDate(boolean isFromDate){
        return getDateForKey((isFromDate) ? PREF_DOC_DATE_FROM_KEY : PREF_DOC_DATE_TO_KEY);
    }
    
    /**
     * If dateFrom saving value is empty, method return  default localDate value.
     * @param isFromDate True if interests dateFrom value (default min). False if interests dateTo value (default max).
     * @return 
     */
    public LocalDate getDocDateForDB(boolean isFromDate){
        LocalDate date = getDocDate(isFromDate);
        if (date == null){
            date = DateConverter.getInstance().parseDate((isFromDate) ? DATE_BIGGER : DATE_LESS);
        }
        return date;
    }
    
    /**
     *  The method gets saving docInDocDate value.
     * @param isFromDate True if interests dateFrom value. False if interests dateTo value.
     * @return 
     */
    public LocalDate getDocInDocDate(boolean isFromDate){
        return getDateForKey((isFromDate) ? PREF_DOC_IN_DOC_DATE_FROM_KEY : PREF_DOC_IN_DOC_DATE_TO_KEY);
    }
    
    /**
     * If dateFrom saving value is empty, method return  default localDate value.
     * @param isFromDate True if interests dateFrom value (default min). False if interests dateTo value (default max).
     * @return 
     */
    public LocalDate getDocInDocDateForDB(boolean isFromDate){
        LocalDate date = getDocInDocDate(isFromDate);
        if (date == null){
            date = DateConverter.getInstance().parseDate((isFromDate) ? DATE_BIGGER : DATE_LESS);
        }
        return date;
    }
    
    private LocalDate getDateForKey(String key){
        String dateStr = getStringFromPref(key);
        return DateConverter.getInstance().parseDate(dateStr);
    }
    
    /**
     * Returns saving account id. Zero - if no value is saving.
     * @return 
     */
    public int getSelectedAccountId(){
        return getIntFromPref(PREF_ACCOUNT_ID_KEY);
    }
    
    /**
     * Returns saving currency iso. Null if nothing is saving.
     * @return 
     */
    public String getSelectedCurrencyIso(){
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
    /**
     * Returns saving docCode description. Null if nothing is saving.
     * @return 
     */
    public String getSelectedDocCode(){
        return getStringFromPref(PREF_DOC_CODE_KEY);
    }
    
    /**
     * Checks is selected concrete or ALL category.
     * @return True - if selected concrete value. False - if selected ALL category.
     */
    public boolean isSelectedConcreteAccount() {
        return getSelectedAccountId() > 0;
    }

    /**
     * Checks is selected concrete or ALL category.
     * @return True - if selected concrete value. False - if selected ALL category.
     */
    public boolean isSelectedConcreteCurrency() {
        String isoFromPrefs = getSelectedCurrencyIso();
        return isoFromPrefs != null && !isoFromPrefs.equals(CurrencyComboBox.categoryALL);
    }

    /**
     * Checks is selected concrete or ALL category.
     * @return True - if selected concrete value. False - if selected ALL category.
     */
    public boolean isSelectedConcreteDocCode() {
        String docCodeFromPref = getSelectedDocCode();
        return docCodeFromPref != null && !docCodeFromPref.equals(DocCodeComboBox.categoryALL);
    }
    
}
