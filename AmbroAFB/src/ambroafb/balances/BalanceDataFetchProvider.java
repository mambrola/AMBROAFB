/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances;

import ambroafb.balances.filter.BalanceFilterModel;
import ambroafb.currencies.CurrencyDataFetchProvider;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.DBClient;
import java.time.LocalDate;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class BalanceDataFetchProvider extends DataFetchProvider {

    // If it will be needed to set JSONObject from outside, must use this static variables:
    public static final String DATE_JSON_KEY = "date";
    public static final String ISO_JSON_KEY = "iso";
    
    private final String DB_FETCH_BALANCES_PROCEDURE = "balance_get";
    private final CurrencyDataFetchProvider currencyDataFetchProvider = new CurrencyDataFetchProvider();
    
    @Override
    public List<Balance> getFilteredBy(JSONObject params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        LocalDate date = (params.has(DATE_JSON_KEY)) ? (LocalDate)params.get(DATE_JSON_KEY) : LocalDate.now();
        String iso = (params.has(ISO_JSON_KEY)) ? params.optString(ISO_JSON_KEY) : currencyDataFetchProvider.getBasicIso();
        return callProcedure(Balance.class, DB_FETCH_BALANCES_PROCEDURE, dbClient.getLang(), date, iso);
    }

    @Override
    public List<Balance> getFilteredBy(FilterModel model) throws Exception {
        BalanceFilterModel filtereModel = (BalanceFilterModel) model;
        JSONObject params = new JSONObject();
        params.put(DATE_JSON_KEY, filtereModel.getDate());          // if getDate() returns null, JSONObject remove entry on DATE_JSON_KEY if it exists and does not put.
        params.put(ISO_JSON_KEY, filtereModel.getCurrencyIso());    // if getCurrencyIso() returns null, JSONObject removes entry on ISO_JSON_KEY if it exists and does not put.
        return getFilteredBy(params);
    }

    @Override
    public Balance getOneFromDB(int recId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
