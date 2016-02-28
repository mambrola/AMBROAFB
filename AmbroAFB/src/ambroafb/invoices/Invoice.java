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
        
        // აქ უნდა გამოვიყენო უნივერსალური ფუნქცია (ცხრილი ან ვიუ, whereპირობა, ორდერ პირობა) იგი დააბრუნებს ArrayList<Object>[]-ს
        // გავივლით მთელ სიგრძეზე და შევქმნით შესაბამის ობიექტებს. გვეცოდინება რომელი ველი რა ტიპისაა და იმის მიხედვით დავკასტავთ 
        
        HashMap<Integer,Invoice> invoices = new HashMap();
        String whereText = invoiceId == 0 ? "" : " where rec_id = " + Integer.toString(invoiceId);
        try {
            Connection conn = GeneralConfig.getInstance().getConnectionToDB();
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM invoices_to_java" +  whereText + " ORDER BY rec_id desc");
            while (resultSet.next()) {
                int recId = resultSet.getInt("rec_id");
                int clientId = resultSet.getInt("client_id");
                String invoiceNumber = resultSet.getString("invoice_number");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                Date beginDate = resultSet.getDate("begin_date");
                Date endDate = resultSet.getDate("end_date");
                String productIds = resultSet.getString("product_ids");
                String productDescrips = resultSet.getString("product_descrips");
                
                invoices.put(recId, new Invoice(recId, invoiceNumber, clientId, beginDate, endDate, firstName, lastName, email, productIds, productDescrips));
            }
        }
        catch (SQLException | NullPointerException ex) {
            Platform.runLater(() -> {
                new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
            });
        }
        return invoices;
    }
    
}
