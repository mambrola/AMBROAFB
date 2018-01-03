/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class BalAccountDataFetchProvider extends DataFetchProvider {

    public BalAccountDataFetchProvider(){
        DB_VIEW_NAME = "bal_accounts_whole";
    }
    
    @Override
    public List<BalanceAccount> getFilteredBy(JSONObject params) throws Exception {
        return getObjectsListFromDBTable(BalanceAccount.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<BalanceAccount> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BalanceAccount getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(BalanceAccount.class, DB_VIEW_NAME, params);
    }
    
}
