/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.in_out.*;
import ambro.ATreeTableView;
import ambro.AView;
import ambroafb.general.Utils;
import ambroafb.invoices.Invoice;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mambroladze
 */
public class Client { 

    // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int clientId;
    
    @AView.Column(width = "24")
    private SimpleBooleanProperty isJur;
    
    @AView.Column(width = "24")
    private SimpleBooleanProperty isRezident;
    
    private String firstName, lastName;
    
    @AView.Column(title = "%descrip", width = "120")
    private SimpleStringProperty descrip;

    @AView.Column(title = "%email", width = "150")
    private SimpleStringProperty email;
    
    private String address, zipCode, city;
    
    @AView.Column(title = "%full_address", width = "200")
    private SimpleStringProperty fullAddress;
    
    private String country_code;
    
    @AView.Column(title = "%country", width = "70")
    private SimpleStringProperty country;
    
    @AView.Column(title = "%pass_number", width = "100")
    private SimpleStringProperty passNumber;
    
    public Client(int ci, boolean ij, boolean ir, String fn, String ln, String e, String a, String zc, String c, String cc, String cd, String pn){
        clientId = ci;
        isJur = new SimpleBooleanProperty(ij);
        firstName = fn;
        lastName = ln;
        descrip = new SimpleStringProperty(firstName + " " + lastName);
        email = new SimpleStringProperty(e);
        address = a;
        zipCode =zc;
        city =c;
        fullAddress = new SimpleStringProperty(address + ", " + zipCode  + ", " + city);
        country = new SimpleStringProperty(cd);
        isRezident = new SimpleBooleanProperty(ir);
        passNumber = new SimpleStringProperty(pn);
    }
    
    
    
    @Override
    public String toString(){
        return descrip+" : "+email+" : "+fullAddress;
    }
            
    public Client dbGetClient (int clientId){ // იქნებ საკმარისი იყოს ResultSet-(ებ)ის დაბრუნება?
        return null;
    }
    
    static HashMap<Integer,Client> dbGetClients(int recId) {
        HashMap<Integer,Client> clients = new HashMap();
        String whereText = recId == 0 ? "" : " where rec_id = " + Integer.toString(recId);
        Utils.getArrayListsFromDB("SELECT * FROM clients_to_java" +  whereText + " ORDER BY rec_id", new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country_code", "country_descrip", "pass_number"}).stream().forEach((row) -> {
            clients.put((int) row[0], new Client((int) row[0], (boolean) row[1], (boolean) row[2], (String) row[3], (String) row[4], (String) row[5], (String) row[6], (String) row[7], (String) row[8], (String) row[9], (String) row[10], (String) row[11]));
        });
        return clients;
    }
    
}
