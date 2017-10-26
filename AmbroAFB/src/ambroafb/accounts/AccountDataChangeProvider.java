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
    
    public AccountDataChangeProvider(){
        
    }
    

//    @Override
//    public void deleteOneFromDB(int recId, String b) {
//        new Thread(() -> {
//            JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
//            
//            try {
//                callProcedure(ACCOUNT_DELETE_CHECK_PROCEDURE, recId);
//                deleteAccount(recId);
//                Platform.runLater(() -> {
//                    if (successAction != null) successAction.accept(null);
//                });
//            } catch (IOException | AuthServerException ex) {
//                Platform.runLater(() -> {
//                    if (errorAction != null) errorAction.accept(ex);
//                });
//            }
//        }).start();
//    }

    
    private void deleteAccount(int recId) {
        System.out.println("<<< call account_delete ... >>>");
//        try {
//            callProcedure(ACCOUNT_DELETE_PROCEDURE, recId);
//        } catch (IOException | AuthServerException ex) {
//        }
    }

    @Override
    public boolean deleteOneFromDB(int recId) throws IOException, AuthServerException {
        return false;
    }

    @Override
    public Account editOneToDB(EditorPanelable object) throws IOException, AuthServerException {
        System.out.println("Account edit method >>>");
        return null;
    }

    @Override
    public Account saveOneToDB(EditorPanelable object) throws IOException, AuthServerException {
        System.out.println("Account save method >>>");
        return null;
    }

    
}
