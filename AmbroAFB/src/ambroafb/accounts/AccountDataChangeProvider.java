/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import java.io.IOException;

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
    public boolean deleteOneFromDB(int recId) throws Exception {
        boolean result = true;
        try {
            callProcedure(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
            callProcedure(ACCOUNT_DELETE_PROCEDURE, recId);
        } catch (AuthServerException | IOException ex){
            result = false;
            throw ex;
        }
        return result;
    }
    
    @Override
    public Account editOneToDB(EditorPanelable object) throws Exception {
        Account result = null;
        Account acc = (Account) object;
        try {
            callProcedure(ACCOUNT_CHECK_PROCEDURE, acc.getRecId(), (int)acc.getAccount(), acc.getClientId(), acc.getBalAccount(), acc.getIso());
            saveSimple(acc, ACCOUNT_TABLE, true);
        } catch (AuthServerException | IOException ex){
            throw ex;
        }
        
        return result;
    }

    @Override
    public Account saveOneToDB(EditorPanelable object) throws Exception {
        Account result = null;
        Account acc = (Account) object;
        try {
            callProcedure(ACCOUNT_CHECK_PROCEDURE, null, (int)acc.getAccount(), acc.getClientId(), acc.getBalAccount(), acc.getIso());
            saveSimple(acc, ACCOUNT_TABLE, true);
        } catch (AuthServerException | IOException ex){
            throw ex;
        }
        
        return result;
    }

    
}
