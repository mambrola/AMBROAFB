/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.currency_rates.filter.CurrencyRateFilterModel;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class CurrencyRateDataFetchProvider extends DataFetchProvider {

    public CurrencyRateDataFetchProvider(){
        DB_VIEW_NAME = "rates_whole";
    }
    
    @Override
    public List<CurrencyRate> getFilteredBy(JSONObject params) throws Exception {
        List<CurrencyRate> currencyRates = getObjectsListFromDBTable(CurrencyRate.class, DB_VIEW_NAME, params);
        currencyRates.sort((CurrencyRate c1, CurrencyRate c2) -> c1.compareTo(c2));
        return currencyRates;
    }

    @Override
    public List<CurrencyRate> getFilteredBy(FilterModel model) throws Exception {
        CurrencyRateFilterModel currencyRateFilterModel = (CurrencyRateFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                .and("date", ">=", currencyRateFilterModel.getFromDateForDB())
                .and("date", "<=", currencyRateFilterModel.getToDateForDB());
        if (currencyRateFilterModel.isSelectedConcreteCurrency()) {
            whereBuilder.and("iso", "=", currencyRateFilterModel.getSelectedCurrencyIso());
        }
        return getFilteredBy(whereBuilder.condition().build());
    }

    @Override
    public CurrencyRate getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(CurrencyRate.class, DB_VIEW_NAME, params);
    }
    
}
