/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.general.DBUtils;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AccountComboBox extends ComboBox<Account> {
    
    private final ObservableList<Account> accounts = FXCollections.observableArrayList();
    private final FilteredList<Account> filteredList = new FilteredList(accounts);
    
    public AccountComboBox(){
        super();
        this.setItems(filteredList);
    }
    
    /**
     * The method fills comboBox by accounts from DB.
     * Note: method starts thread and fetch data in it. If this method call in AccountComboBox  constructor, they may cause problem
     * for other class, where this comboBox use -  it will become that other class fetch thread execute first and after execute this fetch.
     * So the list will not be change from other class.
     * @param consumer The extra action on comboBox filling. If there is no extra action exists, gives null value. 
     */
    public void fillComboBox(Consumer<List<Account>> consumer){
        new Thread(new FetchDataFromDB(consumer)).start();
    }
    
    /**
     *  The method filters  comboBox by ISO and set value if account exists on ISO.
     * @param iso 
     */
    public void filterBy(String iso){
        Account old = getValue();
        filteredList.setPredicate((Account acc) -> {
            if (iso == null || iso.isEmpty()) return true;
            return acc.getIso().equals(iso);
        });
        if (old != null){
            Optional<Account> opt = filteredList.stream().filter((acc) -> acc.getAccount() == old.getAccount()).findFirst();
            setValue((opt.isPresent()) ? opt.get() : null);
        }
        if (iso == null) {
            setValue(null);
        }
    }
    
    /**
     * The method uses to set items in comboBox.
     * @param accounts 
     */
    public void setAccounts(List<Account> accounts){
        this.accounts.setAll(accounts);
    }
    
    /**
     * The method uses to get items from comboBox.
     * @return 
     */
    public ObservableList<Account> getAccounts(){
        return accounts;
    }
    
    private class FetchDataFromDB implements Runnable {

        private final String DB_TABLE_NAME = "accounts";
        private final Consumer<List<Account>> consumer;
        
        public FetchDataFromDB(Consumer<List<Account>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            JSONObject params = new ConditionBuilder().build();
            ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_TABLE_NAME, params);
            accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
            Platform.runLater(() -> {
                accounts.setAll(accountFromDB);
                if (consumer != null){
                    consumer.accept(accountFromDB);
                }
            });
        }
        
    }
}
