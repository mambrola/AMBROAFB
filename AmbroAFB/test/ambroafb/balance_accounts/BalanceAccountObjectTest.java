/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dkobuladze
 */
public class BalanceAccountObjectTest {
    
    private BalanceAccount balAcc;
    
    @Before
    public void setUp() {
        balAcc = new BalanceAccount();
        fillBalAccount(balAcc, 1, 1000, "Descrip");
        balAcc.setRecId(10);
    }
    
    private void fillBalAccount(BalanceAccount balAc, int actPas, int balAcNum, String descrip){
        balAc.setActPas(actPas);
        balAc.setBalAcc(balAcNum);
        balAc.setDescrip(descrip);
    }
    
    
    /**
     * Test of compares method, of class BalanceAccount.
     */
    @Test 
    public void testCompares() {
        BalanceAccount instance = new BalanceAccount();
        fillBalAccount(instance, 1, 1000, "Descrip");
        
        Assert.assertTrue(instance.compares(instance));
    }
    
    
    /**
     * Test of cloneWithoutID method, of class BalanceAccount.
     */
    @Test
    public void testCloneWithoutID() {
        BalanceAccount result = balAcc.cloneWithoutID();
        Assert.assertTrue(result.compares(balAcc) && result.getRecId() != balAcc.getRecId());
    }

    /**
     * Test of cloneWithID method, of class BalanceAccount.
     */
    @Test
    public void testCloneWithID() {
        BalanceAccount result = balAcc.cloneWithID();
        Assert.assertTrue(result.compares(balAcc) && result.getRecId() == balAcc.getRecId());
    }

    /**
     * Test of copyFrom method, of class BalanceAccount.
     */
    @Test
    public void testCopyFrom() {
        BalanceAccount instance = new BalanceAccount();
        instance.copyFrom(balAcc);
        Assert.assertTrue(instance.compares(balAcc));
    }


}
