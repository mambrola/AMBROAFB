/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public class AccountDataChangeProvider extends DataChangeProvider {

    private final String ACCOUNT_DELETE_CHECK_PROCEDURE = "account_delete_check_account";
    private final String ACCOUNT_DELETE_PROCEDURE = "account_delete";
    private final String ACCOUNT_CHECK_PROCEDURE = "account_insert_update_check_account";
    private final String ACCOUNT_TABLE = "accounts";
    
    public AccountDataChangeProvider(){
        
    }
    

    @Override
    public EditorPanelable deleteOneFromDB(int recId) throws Exception {
        callProcedure(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
        callProcedure(ACCOUNT_DELETE_PROCEDURE, recId);
        
        return null;
    }
    
    @Override
    public Account editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Account saveOneToDB(EditorPanelable object) throws Exception {
        Account acc = (Account) object;
        Integer id = (acc.getRecId() == 0) ? null : acc.getRecId();
        callProcedure(ACCOUNT_CHECK_PROCEDURE, id, (int)acc.getAccount(), acc.getClientId(), acc.getBalAccount(), acc.getIso());
        return saveSimple(acc, ACCOUNT_TABLE, true);
    }

    
}
