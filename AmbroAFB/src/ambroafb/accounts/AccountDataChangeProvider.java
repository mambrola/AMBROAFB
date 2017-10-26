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
    private final String ACCOUNT_INSERT_PROCEDURE = "account_insert_update_check_account";
    
    public AccountDataChangeProvider(){
        
    }
    

    @Override
    public boolean deleteOneFromDB(int recId) throws IOException, AuthServerException {
        boolean result = true;
        try {
            callProcedure(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
        } catch (IOException | AuthServerException ex){
            result = false;
            throw ex;
        }
        deleteAccount(recId);
        return result;
    }
    
    private void deleteAccount(int recId) {
        System.out.println("<<< call account_delete ... >>>");
//        try {
//            callProcedure(ACCOUNT_DELETE_PROCEDURE, recId);
//        } catch (IOException | AuthServerException ex) {
//        }
    }


    @Override
    public Account editOneToDB(EditorPanelable object) throws IOException, AuthServerException {
        Account result = null;
        Account acc = (Account) object;
        try {
            result = getObject(Account.class, ACCOUNT_INSERT_PROCEDURE, acc.getRecId(), (int)acc.getAccount(), acc.getClientId(), acc.getbalAccount(), acc.getIso());
        } catch (IOException | AuthServerException ex){
            throw ex;
        }
        return result;
    }

    @Override
    public Account saveOneToDB(EditorPanelable object) throws IOException, AuthServerException {
        Account result = null;
        Account acc = (Account) object;
        try {
            result = getObject(Account.class, ACCOUNT_INSERT_PROCEDURE, null, (int)acc.getAccount(), acc.getClientId(), acc.getbalAccount(), acc.getIso());
        } catch (IOException | AuthServerException ex){
            throw ex;
        }
        return result;
    }

    
}
