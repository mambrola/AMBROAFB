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
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AccountComboBox extends ComboBox<Account> {
    
    public AccountComboBox(){
        super();
        
        new Thread(new FetchDataFromDB(this.getItems())).start();
    }
    
    private class FetchDataFromDB implements Runnable {

        private final String DB_TABLE_NAME = "accounts";
        private final ObservableList<Account> accounts;
        
        public FetchDataFromDB(ObservableList<Account> accounts){
            this.accounts = accounts;
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
