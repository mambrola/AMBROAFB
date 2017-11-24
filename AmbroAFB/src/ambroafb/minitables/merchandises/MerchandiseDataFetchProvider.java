/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class MerchandiseDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "process_merchandises";
    
    @Override
    public List<Merchandise> getFilteredBy(JSONObject params) throws Exception {
        List<Merchandise> merchandises = getObjectsListFromDBTable(Merchandise.class, DB_TABLE_NAME, params);
        return merchandises;
    }

    @Override
    public List<Merchandise> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Merchandise getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Merchandise.class, DB_TABLE_NAME, params);
    }
    
}
