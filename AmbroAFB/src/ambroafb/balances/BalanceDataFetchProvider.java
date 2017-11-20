/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances;

import ambroafb.currencies.CurrencyDataFetchProvider;
import ambroafb.general.DateConverter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.DBClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class BalanceDataFetchProvider extends DataFetchProvider {

    public static final String DATE_JSON_KEY = "date";
    public static final String ISO_JSON_KEY = "iso";
    private final String DB_FETCH_BALANCES_PROCEDURE = "balance_get";
    private final CurrencyDataFetchProvider currencyDataFetchProvider = new CurrencyDataFetchProvider();
    
    @Override
    public List<Balance> getFilteredBy(JSONObject params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        LocalDate date = LocalDate.now();
        String iso = currencyDataFetchProvider.getBasicIso();
        if (params.length() != 0){
            date = DateConverter.getInstance().parseDate(params.optString(DATE_JSON_KEY));
            iso = params.optString(ISO_JSON_KEY);
        }
        System.out.println(String.format("dbClient.getLang(): %s, date: %s, iso: %s", dbClient.getLang(), date.toString(), iso));
        return callProcedure(Balance.class, DB_FETCH_BALANCES_PROCEDURE, dbClient.getLang(), date, iso);
    }

    @Override
    public List<Balance> getFilteredBy(FilterModel model) throws Exception {
        return new ArrayList<>();
    }

    @Override
    public Balance getOneFromDB(int recId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
