/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.DBUtils;
import ambroafb.general.exceptions.DBActionException;
import ambroafb.general.exceptions.ExceptionsFactory;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ParamGeneralDataChangeProvider extends DataChangeProvider {

    private final String DB_INSERT_UPDATE_PROC_NAME = "process_general_param_insert_update";
    private final String DB_TABLE_NAME = "process_general_params";
    private final String DB_SELECT_PROC_NAME = "process_general_param_select";
    
    private final DataFetchProvider dataFetchProvider;
    
    public ParamGeneralDataChangeProvider(DataFetchProvider dataFetchProvider){
        this.dataFetchProvider = dataFetchProvider;
    }
    
    @Override
    public ParamGeneral deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public ParamGeneral editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public ParamGeneral saveOneToDB(EditorPanelable object) throws Exception {
        ParamGeneral result = null;
        try {
            result = saveObjectByProcedure((ParamGeneral)object, DB_INSERT_UPDATE_PROC_NAME);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthServerException ex) {
            Exception exception = ExceptionsFactory.getAppropriateException(ex);
            if (exception instanceof DBActionException) {
                String[] ids = getConflictedIds(exception.getMessage());
                JSONObject entryForParamGenerals = getAskForDB(ids);
                List<ParamGeneral> conflicteds = dataFetchProvider.getFilteredBy(entryForParamGenerals);
                String newMassage = exception.getLocalizedMessage();
                newMassage += conflicteds.stream().map((paramGeneral) -> paramGeneral.toString()).reduce("", (t, u) -> t += u);
                ((DBActionException) exception).setMessage(newMassage);
            }
            throw exception;
        }
        return result;
    }
    
    //  Parses rec_ids from DB error message.
    private String[] getConflictedIds(String errorMsg){
        String startStr = ": ";
        String endStr = ";";
        int startIndex = errorMsg.indexOf(startStr) + startStr.length();
        int endIndex = errorMsg.indexOf(endStr);
        return errorMsg.substring(startIndex, endIndex).split(",");
    }
    
    // Creates JSON that will use to fetch ParamGeneral object properties.
    private JSONObject getAskForDB(String[] ids){
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        for (int i = 0; i < ids.length - 1; i++) {
            whereBuilder.or(DB_ID, "=", ids[i]);
        }
        return whereBuilder.condition().build();
    }
}
