/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.in_outs;

import ambroafb.currencies.CurrencyDataFetchProvider;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.in_outs.filter.InOutFilterModel;
import authclient.db.DBClient;
import java.time.LocalDate;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class InOutDataFetchProvider extends DataFetchProvider {

    // If it will be needed to set JSONObject from outside, must use this static variables:
    public static final String FROM_DATE_JSON_KEY = "from_date";
    public static final String TO_DATE_JSON_KEY = "to_date";
    public static final String ISO_JSON_KEY = "iso";
    
    private final String DB_FETCH_BALANCES_PROCEDURE = "inout_get";
    private final CurrencyDataFetchProvider currencyDataFetchProvider = new CurrencyDataFetchProvider();
    
    @Override
    public List<InOut> getFilteredBy(JSONObject params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        LocalDate fromDate = (params.has(FROM_DATE_JSON_KEY)) ? (LocalDate) params.opt(FROM_DATE_JSON_KEY) : null;
        LocalDate toDate = (params.has(TO_DATE_JSON_KEY)) ? (LocalDate) params.opt(TO_DATE_JSON_KEY) : null;
        String iso = (params.has(ISO_JSON_KEY)) ? params.getString(ISO_JSON_KEY) : currencyDataFetchProvider.getBasicIso();
        return callProcedure(InOut.class, DB_FETCH_BALANCES_PROCEDURE, dbClient.getLang(), fromDate, toDate, iso);
    }

    @Override
    public List<InOut> getFilteredBy(FilterModel model) throws Exception {
        InOutFilterModel filtereModel = (InOutFilterModel) model;
        JSONObject params = new JSONObject();
        params.put(FROM_DATE_JSON_KEY, filtereModel.getFromDateForDB());
        params.put(TO_DATE_JSON_KEY, filtereModel.getToDateForDB());
        if (filtereModel.isSelectedConcreteCurrency()){
            params.put(ISO_JSON_KEY, filtereModel.getCurrencyIso());
        }
        return getFilteredBy(params);
    }

    @Override
    public InOut getOneFromDB(int recId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
