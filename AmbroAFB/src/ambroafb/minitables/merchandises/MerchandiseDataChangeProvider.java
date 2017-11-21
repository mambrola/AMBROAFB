/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class MerchandiseDataChangeProvider extends DataChangeProvider {

    private final String INSERT_UPDATE_PROCEDURE = "process_merchandise_insert_update";
    private final String DELETE_PROCEDURE = "process_merchandise_delete";
    
    @Override
    public Merchandise deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
        return null;
    }

    @Override
    public Merchandise editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Merchandise saveOneToDB(EditorPanelable object) throws Exception {
        return saveObjectByProcedure(object, INSERT_UPDATE_PROCEDURE);
    }
    
}
