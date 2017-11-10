/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class CountryDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "countries";
    
    public CountryDataFetchProvider(){
    
    }
    
    @Override
    public List<Country> getFilteredBy(JSONObject params) throws Exception {
        ArrayList<Country> countries = DBUtils.getObjectsListFromDB(Country.class, DB_TABLE_NAME, params);
        countries.sort((Country c1, Country c2) -> c1.compareByDescrip(c2));
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
    
}
