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

    private final String DB_TABLE_NAME = "process_attitudes";
    
    @Override
    public Attitude deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public Attitude editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Attitude saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((Attitude)object, DB_TABLE_NAME, true);
    }
    
}
