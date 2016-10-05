/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.GeneralConfig;
import ambroafb.general.TestDataFromDB;
import ambroafb.phones.Phone;
import ambroafb.general.Utils;
import authclient.AuthServerException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */

public class Client extends EditorPanelable{

    // ამ ველებს ჯერჯერობით არსად არ ვიყენებთ მაგრამ json-ში მოდის და ერორი რო არ ამოაგდოს მაგიტო საჭიროა რომ არსებობდნენ
    public String payPal, createdDate;
    
    private final StringProperty status;

    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private final SimpleBooleanProperty isJur;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
    private final SimpleBooleanProperty isRez;

    private final SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = "152")
    @JsonIgnore
    private final StringExpression descrip;

    @AView.Column(title = "%email", width = "170")
    private final SimpleStringProperty email;

    private final SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "270")
    @JsonIgnore
    private final StringExpression fullAddress;

    private final ObjectProperty<Country> country;

    @AView.Column(title = "%country", width = "80")
    @JsonIgnore
    private final SimpleStringProperty countryDescrip;

    @AView.Column(title = "%id_number", width = "100")
    @JsonProperty("passNumber")
    private final SimpleStringProperty IDNumber;

    @AView.Column(title = "%phones", width = "300")
    @JsonIgnore
    private final SimpleStringProperty phoneNumbers;

    @JsonProperty("phoneNumbers")
    private final ObservableList<Phone> phoneList;

    @AView.Column(title = "%fax", width = "80")
    private final SimpleStringProperty fax;
    
    @AView.Column(title = "www address", width = "100")
    private final SimpleStringProperty www;

    private static final Country DEFAULT_COUNTRY = new Country("GE", "Georgia");
    
    public Client() {
        isJur =             new SimpleBooleanProperty();
        isRez =             new SimpleBooleanProperty();
        firstName =         new SimpleStringProperty("");
        lastName =          new SimpleStringProperty("");
        descrip = Utils.avoidNull(firstName).concat(" ").concat(Utils.avoidNull(lastName));
        email =             new SimpleStringProperty("");
        address =           new SimpleStringProperty("");
        zipCode =           new SimpleStringProperty("");
        city =              new SimpleStringProperty("");
        fullAddress = Utils.avoidNull(address).concat(", ").concat(Utils.avoidNull(zipCode)).concat(", ").concat(Utils.avoidNull(city));
        country =           new SimpleObjectProperty<>();
        country.set(DEFAULT_COUNTRY);
        isRez.set(country.get().getCode().equals("GE"));
        countryDescrip =    new SimpleStringProperty("");
        IDNumber =          new SimpleStringProperty("");
        phoneList = FXCollections.observableArrayList();
        phoneNumbers =      new SimpleStringProperty("");
        fax =               new SimpleStringProperty("");
        status = new SimpleStringProperty("new");
        www = new SimpleStringProperty("");

        phoneList.addListener((ListChangeListener.Change<? extends Phone> c) -> {
            rebindPhoneNumbers();
        });
        rebindPhoneNumbers();

        country.addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            rebindCountry();
        });
        countryDescrip.set(country.get().codeProperty().concat("   ").concat(country.get().descripProperty()).get());
    }
    
    private void rebindPhoneNumbers() {
        phoneNumbers.unbind();
        phoneNumbers.bind(phoneList
                .stream()
                .map(Phone::numberProperty)
                .reduce(new SimpleStringProperty(""), (StringProperty t, StringProperty u) -> {
                    SimpleStringProperty p = new SimpleStringProperty();
                    p.bind(
                            t.concat(
                                    Bindings.createStringBinding(() -> t.get().isEmpty() ? "" : ", ", t.isEmpty())
                            ).concat(u));
                    return p;
                }));
    }

    private void rebindCountry() {
        countryDescrip.unbind();
        if (country.get() != null) {
            countryDescrip.bind(country.get().codeProperty().concat("   ").concat(country.get().descripProperty()));
        }
    }
    
    // DBService methods:
    public static List<Client> getAllFromDB() {
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("clients").getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {
            });
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static List<Client> getFilteredFromDB(JSONObject filter) {
        List<Client> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            String dateFrom = filter.getString("dateBigger");
            String dateTo = filter.getString("dateLess");
            int jurid = filter.getInt("juridical");
            String isJur = (jurid == 2) ? " is_jur = 0 or is_jur = 1 " : " is_jur = " + jurid + " ";
            Country country = (Country)filter.get("country");
            String countryCode = (country == null || country.getCode().equals("ALL"))? "" : " country_code = '" + ((Country)filter.get("country")).getCode() + "' and ";
//            String status = filter.getString("status") == null ? "" : " status = '" + filter.getString("status") + "' and ";
            int rez = filter.getInt("rezident");
            String isRez = (rez == 2) ? "is_rezident = 0 or is_rezident = 1 " : " is_rezident = " + rez + " ";
            
            String query = "select * from clients_whole " +
                            " where created_date >= '" + dateFrom + "' and created_date <= '" + dateTo + "' and " +
                                   isJur + " and " + countryCode + isRez;
            System.out.println("query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                Client client = new Client();
                client.setRecId(rs.getInt(1));
                client.setEmail(rs.getString(2));
                client.setIsJur(rs.getBoolean(3));
                client.setFirstName(rs.getString(4));
                client.setLastName(rs.getString(5));
                client.setAddress(rs.getString(6));
                client.setZipCode(rs.getString(7));
                client.setCity(rs.getString(8));
                Country c = new Country();
                c.setCode(rs.getString(9));
                client.setCountry(c);
                client.setIsRez(rs.getBoolean(10));
                client.setIDNumber(rs.getString(11));
                client.setFax(rs.getString(12));
                client.setWww(rs.getString(13));
                client.createdDate = rs.getString(14);
                String phones = rs.getString(16);
//                JSONArray phonesArray = new JSONArray(phones);
//                for (int i = 0; i < phonesArray.length(); i++) {
//                    JSONObject object = (JSONObject)phonesArray.get(i);
//                    String number = object.getString("number");
//                    Phone phone = new Phone(number);
//                    client.getPhoneList().add(phone);
//                }
                
                result.add(client);
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
//        try {
//            String data = GeneralConfig.getInstance().getServerClient().get(
//                    "clients/filter?dateFrom=" + filter.getString("dateBigger") + "&dateTo=" + filter.getString("dateLess")
//            );
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {
//            });
//        } catch (IOException | KFZClient.KFZServerException | JSONException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new ArrayList<>();
    }
    
    public static List<String> getStatuses(){
        ArrayList<String> result = new ArrayList<>();
        try {
            JSONArray statuses = new JSONArray(GeneralConfig.getInstance().getAuthClient().get("/clients/statuses").getDataAsString());
            for (int i = 0; i < statuses.length(); i++){
                String status = statuses.getString(i).trim();
                if (!status.isEmpty())
                    result.add(status);
            }
        } catch (JSONException | IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static Client getOneFromDB(int id) {
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("clients/" + id).getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Client.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Client").showAlert();
        }
        return null;
    }

    public static Client saveOneToDB(Client client) {
        if (client == null) return null; 
        try {
            String resource = "clients" + (client.recId > 0 ? "/" + client.recId : "");
            String method = client.recId > 0 ? "PUT" : "POST";
            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String client_str = mapper.writeValueAsString(client);
            
            String res_str = GeneralConfig.getInstance().getAuthClient().call(resource, method, client_str).getDataAsString();
            Client res = mapper.readValue(res_str, Client.class);
            client.copyFrom(res);
            if(client.getRecId() <= 0)
                client.setRecId(res.getRecId());
            return client;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Client").showAlert();
        }
        return null;
    }

    public static boolean deleteOneFromDB(int id) {
        try {
            GeneralConfig.getInstance().getAuthClient().delete("clients/" + id);
            return true;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Client").showAlert();
        }
        return false;
    }

    
    //Properties getters:
    
    public SimpleBooleanProperty isJurProperty() {
        return isJur;
    }

    public SimpleBooleanProperty isRezProperty() {
        return isRez;
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public StringExpression descripProperty() {
        return descrip;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public SimpleStringProperty zipCodeProperty() {
        return zipCode;
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }
    
    public StringExpression fullAddressProperty() {
        return fullAddress;
    }

    public ObjectProperty<Country> countryProperty() {
        return country;
    }

    public SimpleStringProperty IDNumberProperty() {
        return IDNumber;
    }

    public StringExpression phoneNumbersProperty() {
        return phoneNumbers;
    }

    public SimpleStringProperty faxProperty() {
        return fax;
    }
    
    public StringProperty statusProperty(){
        return status;
    }

    public StringProperty wwwProperty(){
        return www;
    }
    
    
    // Getters:
    
    public boolean getIsJur() {
        return isJur.get();
    }

    public boolean getIsRez() {
        return isRez.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getDescrip() {
        return descrip.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getFullAddress() {
        return fullAddress.get();
    }
    
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public Country getCountry() {
        return country.get();
    }

    public ObservableList<Phone> getPhoneList() {
        return phoneList;
    }

    public String getPhoneNumbers() {
        return phoneNumbers.get();
    }

    public String getFax() {
        return fax.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getZipCode() {
        return zipCode.get();
    }

    @JsonProperty("passNumber")
    public String getIDNumber() {
        return IDNumber.get();
    }
    
    public String getWww(){
        return www.get();
    }
    

     // Setters:
    public final void setIsJur(boolean isJur) {
        this.isJur.set(isJur);
    }

    public final void setIsRez(boolean isRez) {
        this.isRez.set(isRez);
    }

    public final void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public final void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public final void setEmail(String email) {
        this.email.set(email);
    }

    public final void setAddress(String address) {
        this.address.set(address);
    }

    public final void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public final void setCity(String city) {
        this.city.set(city);
    }

    public final void setCountry(Country country) {
        this.country.set(country);
    }

    @JsonProperty("passNumber")
    public final void setIDNumber(String IDNumber) {
        this.IDNumber.set(IDNumber);
    }

    public final void setPhoneList(Collection<Phone> phoneList) {
        this.phoneList.setAll(phoneList);
    }

    public final void setFax(String fax) {
        this.fax.set(fax);
    }
    
    public void setWww(String www){
        this.www.set(www);
    }
    
    
    public boolean compares(Client other){
        boolean fieldsCompareResult =   this.isJur.get() == other.getIsJur() &&
                                        this.isRez.get() == other.getIsRez() && 
                                        this.firstName.get().equals(other.getFirstName()) &&
                                        this.lastName.get().equals(other.getLastName()) &&
                                        this.email.get().equals(other.getEmail())    &&
                                        this.www.get().equals(other.getWww()) &&
                                        this.address.get().equals(other.getAddress()) &&
                                        this.zipCode.get().equals(other.getZipCode()) &&
                                        this.city.get().equals(other.getCity()) &&
                                        this.country.get().compares(other.getCountry()) &&
                                        this.IDNumber.get().equals(other.getIDNumber()) &&
                                        this.fax.get().equals(other.getFax()) &&
                                        this.status.get().equals(other.getStatus());
        boolean equalsPhones = Phone.compareLists(phoneList, other.getPhoneList());
        return fieldsCompareResult && equalsPhones;
    }

    // Override methods:
    
    @Override
    public Client cloneWithoutID() {
        Client clone = new Client();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Client cloneWithID() {
        Client clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable object) {
        Client other = (Client) object;
        setIsJur(other.getIsJur());
        setIsRez(other.getIsRez());
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setEmail(other.getEmail());
        setWww(other.getWww());
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        setCountry(other.getCountry());
        setIDNumber(other.getIDNumber());
        getPhoneList().setAll(other.getPhoneList()
                .stream()
                .map((Phone t) -> new Phone(t.getRecId(), t.getNumber()))
                .collect(Collectors.toList())
        );
        setFax(other.getFax());
        setStatus(other.getStatus());
        this.createdDate = other.createdDate;
    }

    @Override
    public String toStringForSearch(){
        String phones = "";
        phones = phoneList.stream().map((phoneNumber) -> phoneNumber.getNumber() + " ").reduce(phones, String::concat);

        String result = firstName.concat(" " + lastName.get())
                                .concat(" " + email.get()).concat(" " + address.get())
                                .concat(" " + city.get())
                                .concat(" " + country.getName())
                        .get();
        return (result + phones);
    }
    

    public static class FirmPersonCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {

        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
                private final ImageView view = new ImageView();

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

    public static class RezCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {

        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            return new TableCell<Client, Boolean>() {
                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    setText(empty ? null : (isFirm ? "Rz" : null));
                }
            };
        }
    }
}
