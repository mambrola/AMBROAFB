/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class AttitudeDataChangeProvider extends DataChangeProvider {

//    private final String DB_TABLE_NAME = "process_attitudes";
    private final String DELETE_PROCEDURE = "process_attitude_delete";
    private final String DB_INSERT_UPDATE = "process_attitude_insert_update";
    
    @Override
    public Attitude deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
        return null;
    }

    @Override
    public Attitude editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Attitude saveOneToDB(EditorPanelable object) throws Exception {
        return saveObjectByProcedure((Attitude)object, DB_INSERT_UPDATE);
    }
    
}
