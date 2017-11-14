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

    private final String DB_TABLE_NAME = "products";
    
    @Override
    public Product deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public Product editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Product saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((Product)object, DB_TABLE_NAME, true);
    }
    
}
