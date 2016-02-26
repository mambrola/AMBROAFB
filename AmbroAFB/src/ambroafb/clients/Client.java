/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.in_out.*;
import ambro.ATreeTableView;
import ambro.AView;
import java.text.DecimalFormat;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mambroladze
 */
public class Client {
    
    public int clientId;
    
    @AView.Column(width = "24")
    public SimpleBooleanProperty isJur;
    
    @AView.Column(width = "24")
    public SimpleBooleanProperty isRezident;
    
    @AView.Column(title = "%first_name", width = "120")
    public SimpleStringProperty first_name;
    
    @AView.Column(title = "%last_name", width = "120")
    public SimpleStringProperty last_name;
    
    @AView.Column(title = "%email", width = "150")
    public SimpleStringProperty email;
    
    private final String address, zip_code, city;
    
    @AView.Column(title = "%full_address", width = "200")
    public SimpleStringProperty fullAddress;
    
    private final String country_code;
    
    @AView.Column(title = "%country", width = "70")
    public SimpleStringProperty country;
    
    @AView.Column(title = "%pass_number", width = "100")
    public SimpleStringProperty passNumber;
    
    public Client(boolean ij, String fn, String ln, String e, String a, String zc, String c, String cc, boolean ir, String pn){
        isJur = new SimpleBooleanProperty(ij);
        first_name = new SimpleStringProperty(fn);
        last_name = new SimpleStringProperty(ln);
        email = new SimpleStringProperty(e);
        address = a;
        zip_code = zc;
        city = c;
        fullAddress = new SimpleStringProperty(address + ", " + zip_code  + ", " + city);
        country_code = cc;
        //country = new SimpleStringProperty(getCountryByCode(country_code));
        isRezident = new SimpleBooleanProperty(ir);
        passNumber = new SimpleStringProperty(pn);
    }
    
    
    
    @Override
    public String toString(){
        return first_name+" : "+last_name+" : "+email+" : "+fullAddress;
    }
            
//ამის მაგალითია customTable-ში Person
    
}
