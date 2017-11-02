/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.clients.filter.ClientFilterModel;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ClientDataFetchProvider extends DataFetchProvider {
        
    private final String DB_CLIENT_STATUS_TABLE = "client_status_descrips";

    public ClientDataFetchProvider(){
        DB_VIEW_NAME = "clients_whole";
    }
    
    @Override
    public List<Client> getFilteredBy(JSONObject params) throws IOException, AuthServerException {
        return getObjectsListFromDB(Client.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<Client> getFilteredBy(FilterModel model) throws IOException, AuthServerException {
        final ClientFilterModel clientFilterModel = (ClientFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                                                .and("created_time", ">=", clientFilterModel.getFromDateForDB())
                                                .and("created_time", "<=", clientFilterModel.getToDateForDB());
        if (!clientFilterModel.isJuridicalIndeterminate()) {
            whereBuilder.and("is_jur", "=", clientFilterModel.isJuridicalSelected() ? 1 : 0);
        }
        if (!clientFilterModel.isRezidentIndeterminate()) {
            whereBuilder.and("is_rezident", "=", clientFilterModel.isRezidentSelected() ? 1 : 0);
        }
        if (!clientFilterModel.isTypeIndeterminate()){
            String relation = (clientFilterModel.isTypeSelected()) ? "is not null" : "is null";
            whereBuilder.and("email", relation, "");
        }
        if (clientFilterModel.isSelectedConcreteCountry()){
            whereBuilder.and("country_code", "=", clientFilterModel.getSelectedCountryCode());
        }
        if (clientFilterModel.hasSelectedStatuses()){
            whereBuilder = whereBuilder.andGroup();
            for (ClientStatus clientStatus : clientFilterModel.getSelectedStatuses()) {
                whereBuilder.or("status", "=", clientStatus.getClientStatusId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        
        JSONObject params = whereBuilder.condition().build();
        return getFilteredBy(params);
    }

    @Override
    public <T> T getOneFromDB(int recId) throws IOException, AuthServerException {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Client.class, DB_VIEW_NAME, params);
    }
    
    /**
     *  The method fetches clients statuses  from DB.
     * @return The list of clients statuses
     * @throws IOException
     * @throws AuthServerException 
     */
    public List<ClientStatus> getClientStatuses() throws IOException, AuthServerException{
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return getObjectsListFromDB(ClientStatus.class, DB_CLIENT_STATUS_TABLE, params);
    }
}
