/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.loggings.filter.LoggingFilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class LoggingDataFetchProvider extends DataFetchProvider {

    public LoggingDataFetchProvider(){
        DB_VIEW_NAME = "logins_by_license_whole";
    }
    
    @Override
    public List<Logging> getFilteredBy(JSONObject params) throws Exception {
        List<Logging> loggings = getObjectsListFromDBTable(Logging.class, DB_VIEW_NAME, params);
        return loggings;
    }

    @Override
    public List<Logging> getFilteredBy(FilterModel model) throws Exception {
        LoggingFilterModel logingFilterModel = (LoggingFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                                        .and("login_time", ">=", logingFilterModel.getFromDateForDB())
                                        .and("login_time", "<=", logingFilterModel.getToDateForDB());
        if (logingFilterModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", logingFilterModel.getSelectedClientId());
        }
        return getFilteredBy(whereBuilder.condition().build());
    }

    @Override
    public <T> T getOneFromDB(int recId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
