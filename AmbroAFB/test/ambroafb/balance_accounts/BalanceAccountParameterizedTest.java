/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author dkobuladze
 */
@RunWith(JUnitParamsRunner.class)
public class BalanceAccountParameterizedTest {
    
    private BalanceAccount balAcc;
    
    
    @Before
    public void setUp() {
        balAcc = new BalanceAccount();
    }
    
    @After
    public void tearDown() {
    }
    
    
    /**
     * Test of getActPas method, of class BalanceAccount.
     * Note: State test.
     * @param inputActPas Indicator of active (1 -  active, 2 - passive, 3 - indeterminate).
     * @param exp Expected result.
     */
    @Test
    @Parameters({"1, 1", "2, 2", "3, 3"})
    public void testGetActPas(int inputActPas, int exp) {
        balAcc.setActPas(inputActPas);
        int result = balAcc.getActPas();
        assertTrue(exp == result);
    }
    
    /**
     * Test of getBalAcc method, of class BalanceAccount.
     * Note:  State test.
     * @param balAccNumber Account number.
     */
    @Test
    @Parameters({"1000", "1200", "1122"})
    public void testGetBalAcc(int balAccNumber) {
        balAcc.setBalAcc(balAccNumber);
        Assert.assertEquals(balAcc.getBalAcc(), balAccNumber);
    }
    
    /**
     *   Test of getBalAcc method, when number is incorrect format.
     * Note: State test.
     */
    @Test(expected = NumberFormatException.class)
    public void testGetBalAccError() {
        balAcc.balAccProperty().set("1 000"); // cause NumberFormatException.
        balAcc.getBalAcc();
    }
    
    /**
     * Test of getDescrip method, of class BalanceAccount.
     * Note: State test.
     * @param descrip BalanceAccount object description parameter.
     */
    @Test
    @Parameters({"This is description text!", ""})
    public void testGetDescrip(String descrip) {
        String copyDescrip = descrip.substring(0);
        balAcc.setDescrip(copyDescrip);
        Assert.assertEquals(descrip, balAcc.getDescrip());
    }
    
    /**
     * Test of toStringForSearch method, of class BalanceAccount.
     * @param descrip BalanceAccount object description parameter.
     */
    @Test
    @Parameters({"This is description text!", ""})
    public void testToStringForSearch(String descrip) {
        String copyDescrip = descrip.substring(0);
        balAcc.setDescrip(copyDescrip);
        Assert.assertEquals(descrip, balAcc.toStringForSearch());
    }
    
    /**
     * Test of toString method, of class BalanceAccount.
     * @param descrip BalanceAccount object description parameter.
     */
    @Test
    @Parameters({"This is description text!", ""})
    public void testToString(String descrip) {
        String copyDescrip = descrip.substring(0);
        balAcc.setDescrip(copyDescrip);
        Assert.assertEquals(descrip, balAcc.toString());
    }
    
    
}
