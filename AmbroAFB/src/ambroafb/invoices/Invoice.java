/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.products.Product;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mambroladze
 */
public class Invoice { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int invoiceId;
    
    @AView.Column(title = "%invoice_n", width = "100")
    private SimpleStringProperty invoice_number;
    
    private int clientId;
    
    private String clientFirstName, clientLastName, clientEmail;
    
    @AView.Column(title = "%client", width = "250")
    private SimpleStringProperty client;

    private ArrayList<Integer> productIds;
    
    @AView.Column(title = "%products", width = "400")
    private SimpleStringProperty products;
        
    public Invoice(int ii, String in, int ci, String cfn, String cln, String ce, ArrayList<Integer> p){
        invoiceId = ci;
        invoice_number = new SimpleStringProperty(in);
        clientId = ci;
        clientFirstName = cfn;
        clientLastName = cln; 
        clientEmail = ce;
        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
        productIds = p;
        String ps = "";
        for(int pp : productIds){
            ps = ps + (ps.length() == 0 ? "" : ",   ") + Product.dbGetProduct(pp).getDescrip();
        }
        products = new SimpleStringProperty(ps);
    }
    
    @Override
    public String toString(){
        return invoice_number+" : "+clientEmail+" : "+ productIds.toString();
    }
            
    public Invoice dbGetClient (int invoiceId){ // იქნებ საკმარისი იყოს ResultSet-(ებ)ის დაბრუნება?
        return null;
    }
    
    public static Invoice[] dbGetClient (){
        return null;
    }
    
}
