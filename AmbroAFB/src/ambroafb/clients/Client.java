/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.clients.helper.Status;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.GeneralConfig;
import ambroafb.phones.Phone;
import ambroafb.general.Utils;
import ambroafb.general.image_gallery.ImageGalleryController;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */

public class Client extends EditorPanelable{

    // ამ ველებს ჯერჯერობით არსად არ ვიყენებთ მაგრამ json-ში მოდის და ერორი რო არ ამოაგდოს მაგიტო საჭიროა რომ არსებობდნენ
    public String payPal, createdDate;
    
//    private final StringProperty status;

    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private final SimpleBooleanProperty isJur;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
    private final SimpleBooleanProperty isRezident;

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

//    @JsonProperty("phoneNumbers")
    private final ObservableList<Phone> phones;

    @AView.Column(title = "%fax", width = "80")
    private final SimpleStringProperty fax;
    
    @AView.Column(title = "www address", width = "100")
    private final SimpleStringProperty www;
    
    private final ObjectProperty<Status> status;
    
    private final SimpleStringProperty remark;
    
    private final SimpleStringProperty countryCode;

    private static final Country DEFAULT_COUNTRY = new Country("GE", "Georgia");
    
    private ImageGalleryController clientImageGallery;
    
    private static final String DB_TABLE_NAME = "clients";
    private static final String DB_VIEW_NAME = "clients_whole";
    private static final String DB_STATUS_TABLE = "client_status_descrips";
    
            
    public Client() {
        isJur =             new SimpleBooleanProperty();
        isRezident =             new SimpleBooleanProperty();
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
        isRezident.set(country.get().getCode().equals("GE"));
        countryDescrip =    new SimpleStringProperty("");
        IDNumber =          new SimpleStringProperty("");
        phones = FXCollections.observableArrayList();
        phoneNumbers =      new SimpleStringProperty("");
        fax =               new SimpleStringProperty("");
        status = new SimpleObjectProperty();
        status.set(new Status());
        www = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        countryCode = new SimpleStringProperty("");

        phones.addListener((ListChangeListener.Change<? extends Phone> c) -> {
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
        phoneNumbers.bind(phones
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
            String data = GeneralConfig.getInstance().getDBClient().get(DB_VIEW_NAME).getDataAsString();
            System.out.println("client data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static List<Status> getAllStatusFromDB(){
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
            String data = dbClient.select(DB_STATUS_TABLE, condition.build()).toString();
            System.out.println("client status data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Status>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static List<Client> getFilteredFromDB(JSONObject filter) {
        String dateFrom = filter.optString("dateBigger");
        String dateTo = filter.optString("dateLess");
        ObservableList<Status> status = (ObservableList<Status>)filter.opt("statuses"); // ++++++++++++++++++++++++++++++++
//        System.out.println("client status: " + status);
        int jurid = filter.optInt("juridical");
        Country country = (Country)filter.opt("country");
        int rez = filter.optInt("rezident");
        
        WhereBuilder whereBuilder = new ConditionBuilder().where().and("created_date", ">=", dateFrom).and("created_date", "<=", dateTo);
        if (jurid == 2){
            whereBuilder.andGroup().or("is_jur", "=", 0).or("is_jur", "=", 1);
        }
        else {
            whereBuilder.and("is_jur", "=", jurid);
        }
        if (rez == 2){
            whereBuilder.andGroup().or("is_rezident", "=", 0).or("is_rezident", "=", 1);
        }
        else {
            whereBuilder.and("is_rezident", "=", rez);
        }
        if (country != null && !country.getCode().equals(Country.ALL)){
            whereBuilder.and("country_code", "=", country.getCode());
        }
        if (status != null){
            whereBuilder.andGroup();
            status.stream().forEach((clientStatus) -> {
                whereBuilder.or("status", "=", clientStatus.getClientStatusId());
            });
            
        }
        
        try {
            JSONObject params = whereBuilder.condition().build();
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, params).toString();
            System.out.println("client filtered data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static Client getOneFromDB(int id) {
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().where().and("rec_id", "=", id).condition().build()).toString();
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
//        try {
//            String resource = "clients" + (client.recId > 0 ? "/" + client.recId : "");
//            String method = client.recId > 0 ? "PUT" : "POST";
//            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            String client_str = mapper.writeValueAsString(client);
//            
//            String res_str = GeneralConfig.getInstance().getAuthClient().call(resource, method, client_str).getDataAsString();
//            Client res = mapper.readValue(res_str, Client.class);
//            client.copyFrom(res);
//            if(client.getRecId() <= 0)
//                client.setRecId(res.getRecId());
//            
//            client.getClientImageGallery().sendDataToServer("" + client.getRecId());
//            return client;
//        } catch (IOException | AuthServerException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Client").showAlert();
//        }
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
        return isRezident;
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
    
    public ObjectProperty statusProperty(){
        return status;
    }

    public StringProperty wwwProperty(){
        return www;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public StringProperty countryCodeProperty(){
        return countryCode;
    }
    
    
    // Getters:
    public boolean getIsJur() {
        return isJur.get();
    }

    public boolean getIsRezident() {
        return isRezident.get();
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
    
    public int getStatus() {
        return status.get().getClientStatusId();
    }
    
    public String getStatusDescrip(){
        return status.get().getDescrip();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getCountryCode(){
        return countryCode.get();
    }
    
    
    public void setStatus(int status) {
        this.status.get().setClientStatusId(status);
    }
    
    public void setStatusDescrip(String statusDescrip){
        this.status.get().setDescrip(statusDescrip);
    }

    public Country getCountry() {
        return country.get();
    }

    public ObservableList<Phone> getPhones() {
        return phones;
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
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    public void setCountryCode(String countryCode){
        this.countryCode.set(countryCode);
    }
    
    public ImageGalleryController getClientImageGallery(){
        return clientImageGallery;
    }
    

     // Setters:
    public final void setIsJur(boolean isJur) {
        this.isJur.set(isJur);
    }

    public final void setIsRezident(boolean isRez) {
        this.isRezident.set(isRez);
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

    public final void setPhones(Collection<Phone> phones) {
        this.phones.setAll(phones);
    }

    public final void setFax(String fax) {
        this.fax.set(fax);
    }
    
    public void setWww(String www){
        this.www.set(www);
    }
    
    public void setClientImageGalelry(ImageGalleryController imageGallery){
        this.clientImageGallery = imageGallery;
    }
    
    
    public boolean compares(Client other){
        boolean fieldsCompareResult =   this.isJur.get() == other.getIsJur() &&
                                        this.isRezident.get() == other.getIsRezident() && 
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
        boolean equalsPhones = Phone.compareLists(phones, other.getPhones());
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
        setIsRezident(other.getIsRezident());
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setEmail(other.getEmail());
        setWww(other.getWww());
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        setCountry(other.getCountry());
        setIDNumber(other.getIDNumber());
        getPhones().setAll(other.getPhones()
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
        phones = this.phones.stream().map((phoneNumber) -> phoneNumber.getNumber() + " ").reduce(phones, String::concat);

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
