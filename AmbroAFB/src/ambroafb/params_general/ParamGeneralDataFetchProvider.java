/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.params_general.helper.ParamGeneralDBResponse;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ParamGeneralDataFetchProvider extends DataFetchProvider {

    private final String DB_SELECT_PROC_NAME = "process_general_param_select";
    
    
    @Override
    public List<ParamGeneral> getFilteredBy(JSONObject params) throws Exception {
        ParamGeneralDBResponse paramsGeneralResponse = getParamsGeneral(DB_SELECT_PROC_NAME, params);
        ArrayList<ParamGeneral> paramsGeneral = paramsGeneralResponse.getParamGenerals();
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p1.compareById(p2));
        return paramsGeneral;
    }
    
    public ParamGeneralDBResponse getParamsGeneral(String procedureName, JSONObject params) throws IOException, AuthServerException{
        ParamGeneralDBResponse response = new ParamGeneralDBResponse();
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray resultDB = dbClient.callProcedureAndGetAsJson(procedureName, dbClient.getLang(), params);
        String generalParams = authclient.Utils.toCamelCase(resultDB).toString();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ParamGeneral> generalParamsList = mapper.readValue(generalParams, mapper.getTypeFactory().constructCollectionType(ArrayList.class, ParamGeneral.class));
        response.setParamGenerals(generalParamsList);
        return response;
    }

    @Override
    public List<ParamGeneral> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParamGeneral getOneFromDB(int recId) throws Exception {
        JSONObject json = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, json);
    }

    
}
