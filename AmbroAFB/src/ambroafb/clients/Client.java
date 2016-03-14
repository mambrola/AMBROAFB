/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.general.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
    public SimpleBooleanProperty isJur;
    
    @AView.Column(width = "24")
    public SimpleStringProperty isRezident;
    
    public String firstName, lastName;
    
    @AView.Column(title = "%descrip", width = "120")
    public SimpleStringProperty descrip;

    @AView.Column(title = "%email", width = "150")
    public SimpleStringProperty email;
    
    public String address, zipCode, city;
    
    @AView.Column(title = "%full_address", width = "200")
    public SimpleStringProperty fullAddress;
    
    public String country_code;
    
    @AView.Column(title = "%country", width = "70")
    public SimpleStringProperty country;
    
    @AView.Column(title = "%id_number", width = "100")
    public SimpleStringProperty IDNumber;
    
    public ArrayList<String> phoneList;
    
    @AView.Column(title = "%phone", width = "300")
    public SimpleStringProperty phones;
    
    @AView.Column(title = "%fax", width = "100")
    public SimpleStringProperty fax;
    
    
    public Client(Object[] values){
        System.out.println("values: " + Utils.avoidNullAndReturnBoolean(values[1]) +":"+ values[1]);
        clientId =      Utils.avoidNullAndReturnInt(values[0]);
        isJur =         new SimpleBooleanProperty(Utils.avoidNullAndReturnBoolean(values[1]));
        isRezident =    new SimpleStringProperty(Utils.avoidNullAndReturnBoolean(values[2]) ? "Rz" : "");
        firstName =     Utils.avoidNullAndReturnString(values[3]);
        lastName =      Utils.avoidNullAndReturnString(values[4]);
        descrip =       new SimpleStringProperty(firstName + " " + lastName);
        email =         new SimpleStringProperty(Utils.avoidNullAndReturnString(values[5]));
        address =       Utils.avoidNullAndReturnString(values[6]);
        zipCode =       Utils.avoidNullAndReturnString(values[7]);
        city =          Utils.avoidNullAndReturnString(values[8]);
        fullAddress =   new SimpleStringProperty(address + ", " + zipCode  + ", " + city);
        country_code =  Utils.avoidNullAndReturnString(values[9]);
        country =       new SimpleStringProperty(Utils.avoidNullAndReturnString(values[10]));
        IDNumber =      new SimpleStringProperty(Utils.avoidNullAndReturnString(values[11]));
        phoneList =     new ArrayList<>(Arrays.asList(Utils.avoidNullAndReturnString(values[12]).split(":;:")));
        phones =        new SimpleStringProperty(Utils.avoidNullAndReturnString(values[12]).replaceAll(":;:", ",  "));
        fax =           new SimpleStringProperty(Utils.avoidNullAndReturnString(values[13]));
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
        String[] orderedRequestedFields = new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country_code", "country_descrip", "pass_number", "phones", "fax"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {
            
            System.out.println("row: " + row[12]);
            
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
