/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.countries.Country;
import ambroafb.general.PhoneNumber;
import java.util.Collection;
import java.util.List;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dato
 */
public class ClientTest {
    
    Client personalClient;
    Client firmaClient;
    
    private ObservableList<PhoneNumber> phones;
    
    @Before
    public void setUp() {
        personalClient = new Client();
        initClient(personalClient, true);
        initClient(firmaClient, false);
    }
    
    private void initClient(Client client, boolean isPersonal){
        client.setIsJur(isPersonal);
        client.setIsRez(true);

        String name = isPersonal ? "personal name" : "firma name";
        client.setFirstName(name);
        
        String lastName = isPersonal ? "personal lastName" : "firma lastName";
        client.setLastName(lastName);
        
        client.setEmail("some@gmail.com");
        client.setAddress("some address");
        client.setZipCode("12345");
        client.setCity("some city");
        client.setCountry(new Country("GE", "Georgia"));
        client.setIDNumber("60001151326");
        
        phones = FXCollections.observableArrayList();
        phones.add(new PhoneNumber("22 11 33"));
        client.setPhoneList(phones);
        
        client.setFax("220289");
    }
    
    @Test
    public void compareClientsPhones(){
        Client c = new Client();
        
        ObservableList<PhoneNumber> phones = FXCollections.observableArrayList();
        phones.add(new PhoneNumber(0, "11 22 33"));
        c.setPhoneList(phones);
        
        ObservableList<PhoneNumber> otherPhones = FXCollections.observableArrayList();
        otherPhones.add(new PhoneNumber(0, "11 22 33"));
        
        boolean result = c.comparePhones(phones, otherPhones);
        assertTrue(result);
    }

//    @Test
//    public void testCloneWithoutID() {
//        Client result = personalClient.cloneWithoutID();
//        assertTrue(result.equals(personalClient));
//    }
//
//    @Test
//    public void testCloneWithID() {
//        personalClient.recId = 1;
//        Client result = personalClient.cloneWithID();
//        assertEquals(personalClient.recId, result.recId);
//    }
//
//    @Test
//    public void testToStringWithDescription() {
//        Client instance = new Client();
//        instance.setFirstName("testName");
//        instance.setLastName("testLastName");
//        instance.setEmail("c@gmail.com");
//        
//        String expectedResult = "testName testLastName : c@gmail.com : , , ";
//        String result = instance.toString();
//        assertEquals(expectedResult, result);
//    }
//
//    @Test
//    public void testToStringWithoutNameAndDescription() {
//        Client instance = new Client();
//        instance.setLastName("testLastName");
//        instance.setEmail("c@gmail.com");
//        
//        String expectedResult = " testLastName : c@gmail.com : , , ";
//        String result = instance.toString();
//        assertEquals(expectedResult, result);
//    }
//    
//    @Test
//    public void testToStringWithSomeDescription() {
//        Client instance = new Client();
//        instance.setLastName("testLastName");
//        instance.setEmail("c@gmail.com");
//        instance.setZipCode("12345");
//        
//        String expectedResult = " testLastName : c@gmail.com : , 12345, ";
//        String result = instance.toString();
//        assertEquals(expectedResult, result);
//    }
//    
//    @Test
//    public void testToStringEmptyClient() {
//        Client instance = new Client();
//        
//        String expectedResult = "  : null : , , ";
//        String result = instance.toString();
//        assertEquals(expectedResult, result);
//    }
    
//    @Test
//    public void testGetClients() {
//        List<Client> expResult = null;
//        List<Client> result = Client.getClients();
//        
//        System.out.println("result: " + result.get(0));
////        assertEquals(expResult, result);
//    }

//    /**
//     * Test of getClient method, of class Client.
//     */
//    @Test
//    public void testGetClient() {
//        System.out.println("getClient");
//        int id = 0;
//        Client expResult = null;
//        Client result = Client.getClient(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of saveClient method, of class Client.
//     */
//    @Test
//    public void testSaveClient() {
//        System.out.println("saveClient");
//        Client client = null;
//        Client expResult = null;
//        Client result = Client.saveClient(client);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteClient method, of class Client.
//     */
//    @Test
//    public void testDeleteClient() {
//        System.out.println("deleteClient");
//        int id = 0;
//        boolean expResult = false;
//        boolean result = Client.deleteClient(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
