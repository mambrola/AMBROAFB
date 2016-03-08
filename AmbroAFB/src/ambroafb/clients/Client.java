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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author mambroladze
 */
public class Client { 

    // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int clientId;
    
    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private SimpleBooleanProperty isJur;
    
    @AView.Column(width = "24")
    private SimpleStringProperty isRezident;
    
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
    private SimpleStringProperty passportNumber;
    
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
        isRezident = new SimpleStringProperty(ir ? "Rz" : "");
        passportNumber = new SimpleStringProperty(pn);
    }
    
    public Client(Object[] values){
        clientId = (int) values[0];
        isJur = new SimpleBooleanProperty((boolean) values[1]);
        isRezident = new SimpleStringProperty((boolean) values[2] ? "Rz" : "");
        firstName = (String) values[3];
        lastName = (String) values[4];
        descrip = new SimpleStringProperty(firstName + " " + lastName);
        email = new SimpleStringProperty((String) values[5]);
        address = (String) values[6];
        zipCode =(String) values[7];
        city =(String) values[8];
        fullAddress = new SimpleStringProperty(address + ", " + zipCode  + ", " + city);
        country_code = (String) values[9];
        country = new SimpleStringProperty((String) values[10]);
        passportNumber = new SimpleStringProperty((String) values[11]);
    }
    
    @Override
    public String toString(){
        return descrip+" : "+email+" : "+fullAddress;
    }
            
    public Client dbGetClient (int clientId){ 
        return dbGetClients(clientId).get(clientId);
    }
    
    static HashMap<Integer,Client> dbGetClients(int recId) {
        HashMap<Integer,Client> clients = new HashMap();
        String query = "SELECT * FROM clients_to_java" +  (recId == 0 ? "" : " where rec_id = " + Integer.toString(recId)) + " ORDER BY rec_id";
        String[] orderedRequestedFields = new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country_code", "country_descrip", "pass_number"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {
            clients.put((int) row[0], new Client(row));
        });
        return clients;
    }
 
    public static class FirmPersonCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {
        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
                private ImageView view = new ImageView();
                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        view.setImage(new Image(isFirm ? "/images/office.png" : "/images/person.png"));
                        setGraphic(view);
                    }
                }
            };
            return cell;
        }
    }
}
