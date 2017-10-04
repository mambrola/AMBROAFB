/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.filter;

import ambroafb.accounts.Account;
import ambroafb.docs.DocCode;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.FilterModel;
import java.time.LocalDate;

/**
 *
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
    
    public void setDocDate(boolean isFromDate, LocalDate date){
        String key = (isFromDate) ? PREF_DOC_DATE_FROM_KEY : PREF_DOC_DATE_TO_KEY;
        saveDateForKey(key, date);
    }
    
    public void setDocInDocDate(boolean isFromDate, LocalDate date){
        String key = (isFromDate) ? PREF_DOC_IN_DOC_DATE_FROM_KEY : PREF_DOC_IN_DOC_DATE_TO_KEY;
        saveDateForKey(key, date);
    }
    
    private void saveDateForKey(String key, LocalDate date){
        saveIntoPref(key, (date == null) ? "" : date.toString());
    }
    
    public void setSelectedAccount(Account account){
        if (account != null){
            saveIntoPref(PREF_ACCOUNT_ID_KEY, account.getRecId());
        }
    }
    
    public void setSelectedCurrency(String currency){
        if (currency != null){
            saveIntoPref(PREF_CURRENCY_KEY, currency);
        }
    }
    
    public void setSelectedDocCode(DocCode docCode){
        System.out.println("----------------- doccode: " + docCode);
        if (docCode != null){
            saveIntoPref(PREF_DOC_CODE_KEY, docCode.getDocCode());
        }
    }
    
    public LocalDate getDocDate(boolean isFromdate){
        return getDateForKey((isFromdate) ? PREF_DOC_DATE_FROM_KEY : PREF_DOC_DATE_TO_KEY);
    }
    
    // If date from pref is empty, datebase will give default localDate min value.
    public LocalDate getDocDateForDB(boolean isFromDate){
        LocalDate date = getDocDate(isFromDate);
        if (date == null){
            date = DateConverter.getInstance().parseDate((isFromDate) ? DATE_BIGGER : DATE_LESS);
        }
        return date;
    }
    
    public LocalDate getDocInDocDate(boolean isFromDate){
        return getDateForKey((isFromDate) ? PREF_DOC_IN_DOC_DATE_FROM_KEY : PREF_DOC_IN_DOC_DATE_TO_KEY);
    }
    
    // If date from pref is empty, datebase will give default localDate min value.
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
    
    
    public int getSelectedAccountId(){
        return getIntFromPref(PREF_ACCOUNT_ID_KEY);
    }
    
    public String getSelectedCurrency(){
        return getStringFromPref(PREF_CURRENCY_KEY);
    }
    
    public String getSelectedDocCode(){
        return getStringFromPref(PREF_DOC_CODE_KEY);
    }
    
//    /**
//     * The method fetches accounts and gives them to consumer when done.
//     * @param accounts Accounts comboBox object. It will be filled be account data.
//     * @param consumer The consumer for fetched result  (accounts list). 
//     * The consumer accept method will call in {@link javafx.application.Platform#runLater(java.lang.Runnable)  Platform.runLater}
//     */
//    public void fetchAccounts(AccountComboBox accounts, Consumer<List<Account>> consumer){
//        new Thread(new FetchDataFromDB(accounts, consumer)).start();
//    }

    public boolean isSelectedAccount() {
        return getSelectedAccountId() > 0;
    }

    public boolean isSelectedCurrency() {
        String currency = getSelectedCurrency();
        return currency != null && !currency.isEmpty();
    }

    public boolean isSelectedDocCode() {
        return getSelectedDocCode() != null;
    }
    
    
//    private class FetchDataFromDB implements Runnable {
//
//        private final String DB_TABLE_NAME = "accounts";
//        private final AccountComboBox accounts;
//        private final Consumer<List<Account>> consumer;
//        
//        public FetchDataFromDB(AccountComboBox accounts, Consumer<List<Account>> consumer){
//            this.accounts = accounts;
//            this.consumer = consumer;
//        }
//        
//        @Override
//        public void run() {
//            JSONObject params = new ConditionBuilder().build();
//            ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_TABLE_NAME, params, AfBConsumersManager.getStandardConsumerForDBException());
//            accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
//            Platform.runLater(() -> {
//                accounts.setAccounts(accountFromDB);
//                consumer.accept(accountFromDB);
//            });
//        }
//    }
    
}
