/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.general.DBUtils;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
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
    
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private FilteredList<Account> filteredList = new FilteredList(accounts);
    
    public AccountComboBox(){
        super();
        this.setItems(filteredList);
    }
    
    /**
     * The method fills comboBox by accounts from DB.
     * Note: method starts thread and fetch data in it. If this method call in AccountComboBox  constructor, they may cause problem
     * for other class, where this comboBox use -  it will become that other class fetch thread execute first and after execute this fetch.
     * So the list will not be change from other class.
     */
    public void fillComboBox(){
        new Thread(new FetchDataFromDB(this.getItems())).start();
    }
    
    public void filterBy(String iso){
        if (iso != null){
            filteredList.setPredicate((Account acc) -> {
                if (iso.isEmpty()) return true;
                return acc.getIso().equals(iso);
            });
        }
    }
    
    private class FetchDataFromDB implements Runnable {

        private final String DB_TABLE_NAME = "accounts";
//        private final ObservableList<Account> accounts;
        
        public FetchDataFromDB(ObservableList<Account> accounts){
//            this.accounts = accounts;
        }
        
        @Override
        public void run() {
            JSONObject params = new ConditionBuilder().build();
            ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_TABLE_NAME, params);
            accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
            Platform.runLater(() -> {
                accounts.setAll(accountFromDB);
            });
        }
        
    }
}
