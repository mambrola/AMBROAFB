/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.GeneralConfig;
import ambroafb.general.exceptions.ExceptionsFactory;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class CurrencyDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "currencies";
    
    @Override
    public List<Currency> getFilteredBy(JSONObject params) throws Exception {
        List<Currency> currencies = getObjectsListFromDBTable(Currency.class, DB_TABLE_NAME, params);
        return currencies;
    }

    @Override
    public List<Currency> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Currency getOneFromDB(int recId) throws Exception  {
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
    
    public String getBasicIso() throws Exception {
        JSONArray jsonArr;
        try {
            jsonArr = GeneralConfig.getInstance().getDBClient().select("basic_params", new ConditionBuilder().where().and("param", "=", "rates_basic_iso").condition().build());
            return (String)((JSONObject)jsonArr.opt(0)).opt("value");
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }
    }
    
    public List<Currency> getCurrenciesWithoutBasic() throws Exception {
        return getFilteredBy(new ConditionBuilder().where().and("iso", "!=", getBasicIso()).condition().build());
    }
}
