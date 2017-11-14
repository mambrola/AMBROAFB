/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class BalAccountDataChangeProvider extends DataChangeProvider {

    private final String DB_TABLE_NAME = "bal_accounts";
    
    @Override
    public BalanceAccount deleteOneFromDB(int recId) throws Exception {
        generalDelete(DB_TABLE_NAME, recId);
        return null;
    }

    @Override
    public BalanceAccount editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public BalanceAccount saveOneToDB(EditorPanelable object) throws Exception {
        return saveSimple((BalanceAccount)object, DB_TABLE_NAME, true);
    }
    
}
