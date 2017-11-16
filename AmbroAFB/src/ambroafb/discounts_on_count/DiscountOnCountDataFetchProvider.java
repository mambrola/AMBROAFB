/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DiscountOnCountDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "discounts_on_licenses_count";
    
    @Override
    public List<DiscountOnCount> getFilteredBy(JSONObject params) throws Exception {
        List<DiscountOnCount> discounts = getObjectsListFromDBTable(DiscountOnCount.class, DB_TABLE_NAME, params);
        discounts.sort((DiscountOnCount d1, DiscountOnCount d2) -> d2.compareById(d1));
        return discounts;
    }

    @Override
    public List<DiscountOnCount> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DiscountOnCount getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(DiscountOnCount.class, DB_TABLE_NAME, params);
    }
    
}
