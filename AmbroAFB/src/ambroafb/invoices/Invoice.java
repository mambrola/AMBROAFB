/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.general.Utils;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.DatePicker;

/**
 *
 * @author mambroladze
 */
public class Invoice { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int invoiceId;
    
    @AView.Column(title = "%invoice_n", width = "100")
    private SimpleStringProperty invoiceNumber;
    
    private SimpleIntegerProperty clientId;
    
    @AView.Column(title = "%begin_date", width = "70")
    private LocalDate beginDate;

    @AView.Column(title = "%end_date", width = "70")
    private LocalDate endDate;
    
    private SimpleStringProperty clientFirstName, clientLastName, clientEmail;
    
    @AView.Column(title = "%client", width = "350")
    private SimpleStringProperty client;

    private int[] productIds;
    
    @AView.Column(title = "%products", width = "300")
    private SimpleStringProperty products;
        
    public Invoice(){
        invoiceNumber = new SimpleStringProperty();
        clientId = new SimpleIntegerProperty();
        clientFirstName = new SimpleStringProperty();
        clientLastName = new SimpleStringProperty(); 
        clientEmail = new SimpleStringProperty();
        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
    }
    
    
    
    public Invoice(int ii, String in, int ci, Date bd, Date ed, String cfn, String cln, String ce, String pis, String pds){
        invoiceId = ci;
        invoiceNumber = new SimpleStringProperty(in);
        clientId = new SimpleIntegerProperty(ci);
        beginDate = bd.toLocalDate();
        endDate = ed.toLocalDate();
        clientFirstName = new SimpleStringProperty(cfn);
        clientLastName = new SimpleStringProperty(cln); 
        clientEmail = new SimpleStringProperty(ce);
        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
        String[] pii = pis.split(":;:");
        productIds = new int[pii.length];
        for(int i = 0; i < pii.length; i++)
            productIds[i] = Integer.parseInt(pii[i]);
        products = new SimpleStringProperty(pds.replaceAll(":;:", ",  "));
    }
    
}
