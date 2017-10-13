/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dkobuladze
 */
public class AccountComboBox extends ComboBox<Account> {
    
    public static final String categoryALL = "ALL";
    private final Account accountALL = new Account();
    private final ObservableList<Account> items = FXCollections.observableArrayList();
    private final FilteredList<Account> filteredList = new FilteredList(items);
    
    public AccountComboBox(){
        super();
        this.setItems(filteredList);
        
        accountALL.setDescrip(categoryALL);
    }
    
    /**
     * The method fills comboBox by accounts and category ALL.
     * Note: method starts thread and fetch data in it. If this method call in AccountComboBox  constructor, they may cause problem
     * for other class, where this comboBox use -  it will become that other class fetch thread execute first and after execute this fetch.
     * So the list will not be change from other class.
     * @param extraAction The extra action on comboBox filling. If there is no extra action exists, gives null value.
     */
    public void fillComboBoxWithALL(Consumer<ObservableList<Account>> extraAction){
        Consumer<ObservableList<Account>> addCategoryALL = (accounts) -> {
            accounts.add(0, accountALL);
            setValue(accountALL);
        };
        Consumer<ObservableList<Account>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        new Thread(new FetchDataFromDB(consumer)).start();
    }
    
    /**
     * The method fills comboBox by accounts.
     * @param extraAction The extra action on comboBox filling. If there is no extra action exists, gives null value.
     */
    public void fillComboBoxWithoutALL(Consumer<ObservableList<Account>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
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
        this.items.setAll(accounts);
    }
    
    /**
     * The method uses to get items from comboBox.
     * @return 
     */
    public ObservableList<Account> getAccounts(){
        return items;
    }
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Account>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<Account>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            ArrayList<Account> accountFromDB = Account.getAllFromDB();
            Platform.runLater(() -> {
                items.setAll(accountFromDB);
                if (consumer != null){
                    consumer.accept(items);
                }
            });
        }
        
    }
}
