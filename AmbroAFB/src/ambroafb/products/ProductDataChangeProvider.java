/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class ProductDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "product_delete";
    private final String INSERT_UPDATE_PROCEDURE = "product_insert_update";
    
    @Override
    public Product deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
        return null;
    }

    @Override
    public Product editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Product saveOneToDB(EditorPanelable object) throws Exception {
        return saveObjectByProcedure((Product)object, INSERT_UPDATE_PROCEDURE);
    }
    
}
