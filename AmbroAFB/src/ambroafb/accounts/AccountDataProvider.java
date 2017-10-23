/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AccountDataProvider extends DataProvider {

    private final String ACCOUNT_DELETE_CHECK_PROCEDURE = "account_delete_check_account";
    private final String ACCOUNT_DELETE_PROCEDURE = "account_delete";
    
    public AccountDataProvider(){
        DB_VEIW_NAME = "accounts_whole";
    }
    
    @Override
    public List<EditorPanelable> getListByConditoin(JSONObject params) {
        List<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_VEIW_NAME, params);
        accountFromDB.sort((Account ac1, Account ac2) -> ac1.compareById(ac2));
        return new ArrayList<>(accountFromDB);
    }

    @Override
    public Account getOneFromDB(int recId) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(Account.class, DB_VEIW_NAME, params);
    }

    @Override
    public void deleteOneFromDB(int recId, Consumer<Boolean> successAction, Function<Exception, ButtonType> errorAction) {
        new Thread(() -> {
            JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
            System.out.println("--- account delete method ---");
            
            try {
                deleteObjectFromDB(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
                deleteAccount(recId);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(Boolean.TRUE);
                });
            } catch (IOException | AuthServerException ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) {
                        if (errorAction.apply(ex).equals(ButtonType.OK)) {
                            new Thread(() -> {
                                deleteAccount(recId);
                            }).start();
                        }
                    }
                });
            }
        }).start();
    }
    
    private void deleteAccount(int recId) {
        System.out.println("<<< call account_delete ... >>>");
//        try {
//            deleteObjectFromDB(ACCOUNT_DELETE_PROCEDURE, recId);
//        } catch (IOException | AuthServerException ex) {
//        }
    }

    @Override
    public EditorPanelable editOneToDB(EditorPanelable object) {
        System.out.println("account save method...");
        return null;
    }

    @Override
    public EditorPanelable saveOneToDB(EditorPanelable object) {
        System.out.println("account save method...");
        return null;
    }
    
    
    
}
