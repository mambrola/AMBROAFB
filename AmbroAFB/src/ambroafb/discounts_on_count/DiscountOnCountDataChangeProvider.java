/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class DiscountOnCountDataChangeProvider extends DataChangeProvider {

    private final String DB_TABLE_NAME = "discounts_on_licenses_count";
    
    @Override
    public DiscountOnCount deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public DiscountOnCount editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public DiscountOnCount saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((DiscountOnCount)object, DB_TABLE_NAME, true);
    }
    
}
