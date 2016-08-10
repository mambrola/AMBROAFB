/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.general.interfaces.EditorPanelable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mambroladze
 */
public class Invoice extends EditorPanelable { 

// ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
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
    
    public static ArrayList<Invoice> getAllFromDB (){
        return new ArrayList<>();
//        try {
//            String data = GeneralConfig.getInstance().getServerClient().get("products");
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {});
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }

    @Override
    public EditorPanelable cloneWithoutID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EditorPanelable cloneWithID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toStringForSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
