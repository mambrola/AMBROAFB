/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.general.AlertMessage;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.invoices.helper.InvoiceReissuings;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import org.json.JSONObject;


/**
 *
 * @author mambroladze
 */
public class Invoice extends EditorPanelable { 

// ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
//    public int invoiceId;
    
    public int[] licenses;
    
    @AView.Column(width = "50")
    private final SimpleBooleanProperty isPaid;
    
    @AView.Column(title = "%invoice_n", width = "100")
    private final SimpleStringProperty invoiceNumber;
    
    @AView.Column(title = "%invoice_status", width = "50")
    private final SimpleStringProperty status;
    
    @AView.Column(title = "%created_date", width = "70")
    private LocalDate createdDate;
    
    @AView.Column(title = "%begin_date", width = "70")
    private LocalDate beginDate;

    @AView.Column(title = "%end_date", width = "70")
    private LocalDate endDate;
    
    private final SimpleIntegerProperty clientId;
    private final SimpleStringProperty clientFirstName, clientLastName, clientEmail;
    
    @AView.Column(title = "%client", width = "300")
    private final SimpleStringProperty client;

    private int[] productIds;
    
    @AView.Column(title = "%products", width = "200")
    private SimpleStringProperty products;
        
    private static final String DB_REISSUINGS_TABLE = "invoice_reissuing_descrips";
    
//    private final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//    private final LocalDateStringConverter converter = new LocalDateStringConverter(formater, pattern);
    
    public Invoice(){
        isPaid = new SimpleBooleanProperty();
        invoiceNumber = new SimpleStringProperty();
        status = new SimpleStringProperty();
        clientId = new SimpleIntegerProperty();
        clientFirstName = new SimpleStringProperty();
        clientLastName = new SimpleStringProperty(); 
        clientEmail = new SimpleStringProperty();
        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
    }
    
//    public Invoice(int ii, String in, int ci, Date bd, Date ed, String cfn, String cln, String ce, String pis, String pds){
////        invoiceId = ci;
//        invoiceNumber = new SimpleStringProperty(in);
//        clientId = new SimpleIntegerProperty(ci);
//        createdDate = bd.toLocalDate();
//        endDate = ed.toLocalDate();
//        clientFirstName = new SimpleStringProperty(cfn);
//        clientLastName = new SimpleStringProperty(cln); 
//        clientEmail = new SimpleStringProperty(ce);
//        client = new SimpleStringProperty(clientFirstName + " " + clientLastName + ", " + clientEmail);
//        String[] pii = pis.split(":;:");
//        productIds = new int[pii.length];
//        for(int i = 0; i < pii.length; i++)
//            productIds[i] = Integer.parseInt(pii[i]);
//        products = new SimpleStringProperty(pds.replaceAll(":;:", ",  "));
//    }

    
    // DBService methods:
    public static ArrayList<Invoice> getAllFromDB (){
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("invoices").getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Invoice>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static ArrayList<InvoiceReissuings> getAllIvoiceReissuingsesFromDB(){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return DBUtils.getObjectsListFromDB(InvoiceReissuings.class, DB_REISSUINGS_TABLE, params);
    }
    
    public static Invoice getOneFromDB (int invoiceId){
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("invoices/" + invoiceId).getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Invoice.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Invoice").showAlert();
        }
        return null;
    }

    // Properties getters:
    public SimpleBooleanProperty paidProperty() {
        return isPaid;
    }
    
    public SimpleStringProperty invoiceNumberProperty(){
        return invoiceNumber;
    }
    
    public SimpleStringProperty statusProperty(){
        return status;
    }
    
    public SimpleStringProperty getClient(){
        return client;
    }
            
    
    // Getters:
//    public int getInvoiceId() {
//        return invoiceId;
//    }

    public boolean getIsPaind(){
        return isPaid.get();
    }

    public int[] getLicenses() {
        return licenses;
    }
    
//    public String getCreatedDate(){
//        return converter.toString(createdDate);
//    }
//
//    public String getBeginDate(){
//        return converter.toString(beginDate);
//    }
//    
//    public String getEndDate(){
//        return converter.toString(endDate);
//    }
    
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    
    public String getStatus(){
        return status.get();
    }
    
    
    // Setters:
//    public void setInvoiceId(int invoiceId) {
//        this.invoiceId = invoiceId;
//    }
    
    public void setIsPaid(boolean isPaid) {
        this.isPaid.set(isPaid);
    }
    
    public void setLicenses(int[] licenses) {
        this.licenses = licenses;
    }
    
//    public void setCreatedDate(String createdDate) {
//        this.createdDate = converter.fromString(createdDate);
//    }
//    
//    public void setBeginDate(String beginDate){
//        this.beginDate = converter.fromString(beginDate);
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = converter.fromString(endDate);
//    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
    public void setStatus(String status){
        this.status.set(status);
    }
    
    
    // Methods override:
    @Override
    public Invoice cloneWithoutID() {
        Invoice clone = new Invoice();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Invoice cloneWithID() {
        Invoice clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Invoice invoice = (Invoice) other;
        setIsPaid(invoice.getIsPaind());
        setLicenses(invoice.getLicenses());
        setStatus(invoice.getStatus());
        setInvoiceNumber(invoice.getInvoiceNumber());
//        setCreatedDate(invoice.getCreatedDate());
//        setBeginDate(invoice.getBeginDate());
//        setEndDate(invoice.getEndDate());
    }

    @Override
    public String toStringForSearch() {
//        return getInvoiceNumber().concat(getCreatedDate()).concat(getBeginDate()).concat(getEndDate());
        return "";
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Invoice invoiceBackup = (Invoice) backup;
        return true;
    }

}
