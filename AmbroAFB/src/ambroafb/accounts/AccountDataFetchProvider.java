/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.accounts.filter.AccountFilterModel;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AccountDataFetchProvider extends DataFetchProvider {

    public AccountDataFetchProvider(){
        DB_VIEW_NAME = "accounts_whole";
    }

    @Override
    public List<Account> getFilteredBy(JSONObject params) throws IOException, AuthServerException {
        return getObjectsListFromDB(Account.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<Account> getFilteredBy(FilterModel model) throws IOException, AuthServerException {
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
        if (!accountFiletModel.getTypeIndeterminate()){
            String relation = (accountFiletModel.isTypeSelected()) ? "is null" : "is not null";
            whereBuilder.and("date_close", relation, "");
        }
        // this is extra condition, none of statements was setisfied:
        whereBuilder.and("rec_id", ">", 0);
        JSONObject params = whereBuilder.condition().build();
        return getFilteredBy(params);
    }

    @Override
    public Account getOneFromDB(int recId) throws IOException, AuthServerException {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return getObjectFromDB(Account.class, DB_VIEW_NAME, params);
    }

}
