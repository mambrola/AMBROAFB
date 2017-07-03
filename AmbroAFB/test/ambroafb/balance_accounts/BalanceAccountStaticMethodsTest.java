/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.DBUtils;
import java.util.ArrayList;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author dkobuladze
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DBUtils.class)
public class BalanceAccountStaticMethodsTest {
    
    private BalanceAccount balAcc;
    @Mock
    JSONObject params;
    
    
    @Before
    public void setUp() {
        balAcc = new BalanceAccount();
        
        PowerMockito.mockStatic(DBUtils.class);
    }
    
    /**
     * Behavior test for all BalanceAccount objects from DB. 
     * Checks really invoked getObjectFromDB method or not.
     */
    @Test
    public void testGetAllFromDB() {
        PowerMockito.when(DBUtils.getObjectsListFromDB(BalanceAccount.class, 
                                                       BalanceAccount.DB_TABLE_NAME, 
                                                       params))
                    .thenReturn(new ArrayList<>());
        
        ArrayList<BalanceAccount> result = BalanceAccount.getAllFromDB();
        assertTrue(result.isEmpty());
    }

    
    /**
     * Behavior test for  one BalanceAccount object from DB.
     * Checks really invoked getOneFromDB method or not.
     */
    @Test
    public void testGetOneFromDB() {
        PowerMockito.when(DBUtils.getObjectFromDB(Matchers.eq(BalanceAccount.class), 
                                                  Matchers.eq(BalanceAccount.DB_TABLE_NAME), 
                                                  Matchers.any(JSONObject.class)))
                    .thenReturn(new BalanceAccount());
        
        int recId = 10; //  random id
        BalanceAccount result = BalanceAccount.getOneFromDB(recId);
        assertTrue(result.getRecId() == 0);
    }

    /**
     * Behavior test for  saving  BalanceAccount object to DB.
     * Checks really invoked saveObjectToDBSimple method or not.
     */
    @Test
    public void testSaveOneToDB() {
        PowerMockito.when(DBUtils.saveObjectToDBSimple(Matchers.any(BalanceAccount.class), 
                                                       Matchers.eq(BalanceAccount.DB_TABLE_NAME)))
                    .thenReturn(new BalanceAccount());
        BalanceAccount result = BalanceAccount.saveOneToDB(new BalanceAccount());
        assertTrue(result.getRecId() == 0);
    }

    /**
     * Behavior test for  deleting  BalanceAccount object from DB.
     * Checks really invoked deleteObjectFromDB method or not.
     */
    @Test
    public void testDeleteOneFromDB() {
        int id = 0;
        
        PowerMockito.when(DBUtils.deleteObjectFromDB(BalanceAccount.DB_DELETE_PROC_NAME, 
                                                     BalanceAccount.DB_TABLE_NAME,
                                                     id))
                    .thenReturn(true);
        boolean result = BalanceAccount.deleteOneFromDB(id);
        assertTrue(result);
    }

    
    
}
