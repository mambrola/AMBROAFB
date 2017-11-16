/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambroafb.accounts.Account;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import static ambroafb.general.interfaces.DataFetchProvider.DB_ID;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.licenses.filter.LicenseFilterModel;
import ambroafb.licenses.helper.LicenseStatus;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import java.util.List;
import javafx.collections.ObservableList;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class LicenseDataFetchProvider extends DataFetchProvider {

    private final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public LicenseDataFetchProvider(){
        DB_VIEW_NAME = "licenses_whole";
    }
    
    @Override
    public List<License> getFilteredBy(JSONObject params) throws Exception {
        return getObjectsListFromDBTable(License.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<License> getFilteredBy(FilterModel model) throws Exception {
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        LicenseFilterModel licenseFilterModel = (LicenseFilterModel) model;
        
        if (licenseFilterModel.isSelectedConcreteClient()){
            whereBuilder = whereBuilder.and("client_id", "=", licenseFilterModel.getSelectedClientId());
        }
        if (licenseFilterModel.isSelectedConcreteProduct()){
            whereBuilder = whereBuilder.and("product_id", "=", licenseFilterModel.getSelectedProductId());
        }
        ObservableList<LicenseStatus> statuses = licenseFilterModel.getSelectedStatuses();
        if (!statuses.isEmpty()){
            whereBuilder = whereBuilder.andGroup();
            for (LicenseStatus status : statuses) {
                whereBuilder = whereBuilder.or("status", "=", status.getLicenseStatusId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        if (licenseFilterModel.onlyExtraDays()){
            whereBuilder = whereBuilder.and("additional_days", ">", 0);
        }
        else if (licenseFilterModel.withAndWithoutExtraDays()){
            whereBuilder = whereBuilder.and("additional_days", ">=", 0);
        }
        else {
            whereBuilder = whereBuilder.and("additional_days", "=", 0);
        }
        
        return getFilteredBy(whereBuilder.condition().build());
    }

    @Override
    public License getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Account.class, DB_VIEW_NAME, params);
    }
    
    
    public List<LicenseStatus> getAllLicenseStatusFromDB() throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return getObjectsListFromDBTable(LicenseStatus.class, DB_STATUSES_TABLE_NAME, params);
    }
}
