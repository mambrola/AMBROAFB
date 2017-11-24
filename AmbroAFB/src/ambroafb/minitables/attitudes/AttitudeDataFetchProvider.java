/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class AttitudeDataFetchProvider extends DataFetchProvider {

    private final String DB_TABLE_NAME = "process_attitudes";
    
    @Override
    public List<Attitude> getFilteredBy(JSONObject params) throws Exception {
        List<Attitude> attitudeList = getObjectsListFromDBTable(Attitude.class, DB_TABLE_NAME, params);
        return attitudeList;
    }

    @Override
    public List<Attitude> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Attitude getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Attitude.class, DB_TABLE_NAME, params);
    }
    
}
