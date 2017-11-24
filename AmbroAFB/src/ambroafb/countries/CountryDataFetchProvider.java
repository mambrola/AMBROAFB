/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class CountryDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "countries";
    private final String ORDERED_PROCEDURE = "general_select_ordered";
//    general_select_ordered('countries', 'ka', '{}', 'clients');
    
    public CountryDataFetchProvider(){
    
    }
    
    @Override
    public List<Country> getFilteredBy(JSONObject params) throws Exception {
        List<Country> countries = getObjectsListFromDBTable(Country.class, DB_TABLE_NAME, params);
        return countries;
    }

    @Override
    public List<Country> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Country getOneFromDB(int recId) throws Exception {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    public Country getOneFromDB(String countryCode) throws Exception {
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("code", "=", countryCode).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    private Country getOneFromDBHelper(ConditionBuilder conditionBuilder) throws Exception{
        return getObjectFromDB(Country.class, DB_TABLE_NAME, conditionBuilder.build());
    }
    
    public List<Country> getOrderedByClients() throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        return callProcedure(Country.class, ORDERED_PROCEDURE, DB_TABLE_NAME, dbClient.getLang(), DataFetchProvider.PARAM_FOR_ALL, "clients");
    }
}
