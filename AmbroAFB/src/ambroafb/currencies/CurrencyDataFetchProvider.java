/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class CurrencyDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "currencies";
    
    @Override
    public List<Currency> getFilteredBy(JSONObject params) throws Exception {
        List<Currency> currencies = getObjectsListFromDB(Currency.class, DB_TABLE_NAME, params);
        currencies.sort((Currency c1, Currency c2) -> c1.compareByIso(c2));
        return currencies;
    }

    @Override
    public List<Currency> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Currency getOneFromDB(int recId) throws Exception {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and(DB_ID, "=", recId).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    
    public Currency getOneFromDB (String iso) throws Exception {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("iso", "=", iso).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    private Currency getOneFromDBHelper(ConditionBuilder conditionBuilder) throws Exception {
        JSONObject params = conditionBuilder.build();
        return getObjectFromDB(Currency.class, DB_TABLE_NAME, params);
    }
    
    public List<String> getAllIsoFromDB() throws Exception {
        return getFilteredBy(DataFetchProvider.PARAM_FOR_ALL).stream().map((Currency currency) -> currency.getIso()).collect(Collectors.toList());
    }
}
