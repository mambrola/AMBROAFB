/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.products.Product;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

/**
 *
 * @author mambroladze
 */
public class Invoice { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int invoiceId;
    
    @AView.Column(title = "%invoice_n", width = "100")
    private SimpleStringProperty invoiceNumber;
    
    private int clientId;
    
    @AView.Column(title = "%begin_date", width = "70")
    private LocalDate beginDate;

    @AView.Column(title = "%end_date", width = "70")
    private LocalDate endDate;
    
    private String clientFirstName, clientLastName, clientEmail;
    
    @AView.Column(title = "%client", width = "350")
    private SimpleStringProperty client;

    private int[] productIds;
    
    @AView.Column(title = "%products", width = "300")
    private SimpleStringProperty products;
        
    public Invoice(int ii, String in, int ci, Date bd, Date ed, String cfn, String cln, String ce, String pis, String pds){
        invoiceId = ci;
        invoiceNumber = new SimpleStringProperty(in);
        clientId = ci;
        beginDate = bd.toLocalDate();
        endDate = ed.toLocalDate();
        clientFirstName = cfn;
        clientLastName = cln; 
        clientEmail = ce;
        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
        System.out.println("objectArray: " + client);
        String[] pii = pis.split(":;:");
        productIds = new int[pii.length];
        for(int i = 0; i < pii.length; i++)
            productIds[i] = Integer.parseInt(pii[i]);
        products = new SimpleStringProperty(pds.replaceAll(":;:", ",  "));
    }
    
    public static Invoice dbGetInvoice (int invoiceId){
        return dbGetInvoices(invoiceId).get(invoiceId);
    }
    
    @Override
    public String toString(){
        return invoiceId +":"+ invoiceNumber +":"+ clientId +":"+ client.get() +":"+ Arrays.toString(productIds) +":"+ products.get();
    }
    
    public static HashMap<Integer,Invoice> dbGetInvoices (int invoiceId){
        HashMap<Integer,Invoice> invoices = new HashMap();
        String whereText = invoiceId == 0 ? "" : " where rec_id = " + Integer.toString(invoiceId);
        Utils.getArrayListsByQueryFromDB("SELECT * FROM invoices_to_java" +  whereText + " ORDER BY rec_id desc", new String[]{"rec_id", "invoice_number", "client_id", "begin_date", "end_date", "first_name", "last_name", "email", "product_ids", "product_descrips"}).stream().forEach((row) -> {
            invoices.put((int) row[0], new Invoice((int) row[0], (String) row[1],(int) row[2],(Date) row[3],(Date) row[4],(String) row[5],(String) row[6],(String) row[7],(String) row[8],(String) row[9]));
        });
        return invoices;
    }
    
}
