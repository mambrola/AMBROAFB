/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.FilterModel;
import ambroafb.general.Names;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dkobuladze
 */
public class BalanceAccountsControllerTest {
    
    public BalanceAccountsControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initialize method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testInitialize() {
        URL url = null;
        ResourceBundle rb = null;
        BalanceAccountsController instance = new BalanceAccountsController();
        instance.initialize(url, rb);
    }

    /**
     * Test of reAssignTable method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testReAssignTable() {
        System.out.println("reAssignTable");
        FilterModel model = null;
        BalanceAccountsController instance = new BalanceAccountsController();
        instance.reAssignTable(null, model);
    }

    /**
     * Test of getEditorPanelController method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testGetEditorPanelController() {
        System.out.println("getEditorPanelController");
        BalanceAccountsController instance = new BalanceAccountsController();
        EditorPanelController expResult = null;
        EditorPanelController result = instance.getEditorPanelController();
    }

    /**
     * Test of accountAlreadyExistForCode method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testAccountAlreadyExistForCode() {
        System.out.println("accountAlreadyExistForCode");
        EditorPanelable newElem = null;
        Names.EDITOR_BUTTON_TYPE type = null;
        BalanceAccountsController instance = new BalanceAccountsController();
        boolean expResult = false;
        boolean result = instance.accountAlreadyExistForCode(newElem, type);
    }

    /**
     * Test of accountHasParent method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testAccountHasParent() {
        System.out.println("accountHasParent");
        String balAccCode = "";
        BalanceAccountsController instance = new BalanceAccountsController();
        boolean expResult = false;
        boolean result = instance.accountHasParent(balAccCode);
    }
    
}
