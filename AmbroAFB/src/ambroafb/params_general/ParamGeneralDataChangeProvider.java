/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.AlertMessage;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
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
            String[] errorData = Utils.processAuthServerError(ex);
            if (errorData[0].equals("4042")){ // conflicted entry
                String startStr = ": ";
                int startIndex = errorData[1].indexOf(startStr) + startStr.length();
                int endIndex = errorData[1].indexOf(";");
                String[] ids = errorData[1].substring(startIndex, endIndex).split(",");
                String headerTxt = GeneralConfig.getInstance().getTitleFor("param_general_error") + "\n";
                List<ParamGeneral> entries = selectConflictedEntries(ids);
                String newMsg = entries.stream().map((entry) -> "[" + entry.toString() + "]" + ",\n").reduce("", String::concat);
                AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, newMsg, GeneralConfig.getInstance().getTitleFor("conflict_params_general"));
                alert.setHeaderText(headerTxt);
                alert.showAndWait();
            }
        }
        return result;
    }
    
    private List<ParamGeneral> selectConflictedEntries(String[] ids){
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        for (int i = 0; i < ids.length - 1; i++) {
            whereBuilder.or("rec_id", "=", ids[i]);
        }
        JSONObject conflictedIDs = whereBuilder.condition().build();
        try {
            return getObjectsListFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, conflictedIDs);
        } catch (Exception ex) {
        }
        return new ArrayList<>();
    }
    
}
