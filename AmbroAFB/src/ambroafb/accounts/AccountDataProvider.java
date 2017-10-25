/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.accounts.filter.AccountFilterModel;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
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
    public void getListByConditoin(JSONObject params, Consumer<List<EditorPanelable>> successAction, Consumer<Exception> errorAction) {
        List<Account> accountFromDB;
        try {
            accountFromDB = getObjectsListFromDB(Account.class, DB_VEIW_NAME, params);
            accountFromDB.sort((Account ac1, Account ac2) -> ac1.compareById(ac2));
            Platform.runLater(() -> {
                if (successAction != null) successAction.accept(new ArrayList<>(accountFromDB));
            });
        } catch (IOException | AuthServerException ex) {
            Platform.runLater(() -> {
                if (errorAction != null) errorAction.accept(ex);
            });
        }
    }
    
    @Override
    public void getListBy(FilterModel model, Consumer<List<EditorPanelable>> successAction, Consumer<Exception> errorAction){
        AccountFilterModel accountFiletModel = (AccountFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        
        if (accountFiletModel.isSelectedConcreteCurrency()){
            whereBuilder.and("iso", "=", accountFiletModel.getCurrencyIso());
        }
        if (accountFiletModel.isSelectedConcreteBalAccount()){
            whereBuilder.and("bal_account", "=", accountFiletModel.getBalAccountNumber());
        }
        if (accountFiletModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", accountFiletModel.getClientId());
        }
        if (!accountFiletModel.getTypeIntdeterminate()){
            String relation = (accountFiletModel.isTypeSelected()) ? "is null" : "is not null";
            whereBuilder.and("date_close", relation, "");
        }
        JSONObject params = whereBuilder.condition().build();
        getListByConditoin(params, successAction, errorAction);
    }

    @Override
    public void getOneFromDB(int recId, Consumer<Object> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            JSONObject params = new ConditionBuilder().where().and("rec_id", "=", 100).condition().build();
            try {
                Account accountFromDB = getObjectFromDB(Account.class, DB_VEIW_NAME, params);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(accountFromDB);
                });
            } catch (IOException | AuthServerException ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }

    @Override
    public void deleteOneFromDB(int recId, Consumer<Object> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
            
            try {
                callProcedure(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
                deleteAccount(recId);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(null);
                });
            } catch (IOException | AuthServerException ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    private void deleteAccount(int recId) {
        System.out.println("<<< call account_delete ... >>>");
//        try {
//            callProcedure(ACCOUNT_DELETE_PROCEDURE, recId);
//        } catch (IOException | AuthServerException ex) {
//        }
    }

    @Override
    public void editOneToDB(EditorPanelable object, Consumer<Object> successAction, Consumer<Exception> errorAction) {
        System.out.println("account edit method...");
    }

    @Override
    public void saveOneToDB(EditorPanelable object, Consumer<Object> successAction, Consumer<Exception> errorAction) {
        System.out.println("account save method...");
    }
    
    
    
}
