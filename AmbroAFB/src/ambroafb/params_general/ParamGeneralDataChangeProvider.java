/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.DBUtils;
import ambroafb.general.exceptions.ExceptionsFactory;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

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
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        return result;
    }
    
    
}
