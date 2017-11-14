/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class CurrencyDataChangeProvider extends DataChangeProvider {

    private static final String DB_TABLE_NAME = "currencies";
    
    @Override
    public Currency deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public Currency editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Currency saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((Currency)object, DB_TABLE_NAME, true);
    }
    
}
