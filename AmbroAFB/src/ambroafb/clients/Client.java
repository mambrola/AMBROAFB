/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.clients.filter.ClientFilterModel;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.FilterModel;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */

public class Client extends EditorPanelable{

    // ამ ველებს ჯერჯერობით არსად არ ვიყენებთ მაგრამ json-ში მოდის და ერორი რო არ ამოაგდოს მაგიტო საჭიროა რომ არსებობდნენ
    public String createdDate;  // paypal
    
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

    @AView.Column(title = "%country", width = "50")
    private final SimpleStringProperty countryCode;
    @JsonIgnore
    private final ObjectProperty<Country> country;

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
    
    @JsonIgnore
    private final ObjectProperty<ClientStatus> clientStatus;
    
    private final SimpleStringProperty remark;
    
    // for convert client to json
    private String statusDescrip;
    private int status;

    @JsonIgnore
    private ImageGalleryController clientImageGallery;
    
    private static final String DB_TABLE_NAME = "clients";
    private static final String DB_VIEW_NAME = "clients_whole";
    private static final String DB_STATUS_TABLE = "client_status_descrips";
    
    private static final String IMAGE_OFFICE_URL = "/images/office.png";
    private static final String IMAGE_PERSON_URL = "/images/person.png";
    
    // Every property object has default values because of avoide NullpointerException in compares or any other methods in any case.
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
        country =           new SimpleObjectProperty<>(new Country());
        countryCode =    new SimpleStringProperty("");
        IDNumber =          new SimpleStringProperty("");
        phones = FXCollections.observableArrayList();
        phoneNumbers =      new SimpleStringProperty("");
        fax =               new SimpleStringProperty("");
        clientStatus = new SimpleObjectProperty();
        clientStatus.set(new ClientStatus());
        www = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");

        phones.addListener((ListChangeListener.Change<? extends Phone> c) -> {
            rebindPhoneNumbers();
        });
//        rebindPhoneNumbers();

        country.addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            rebindCountry();
            resetRezident();
        });
        rebindCountry(); // country objectProperty already set country object. So this line is needed to change countryCode column when generate tableView Components.
        
    }
    
    private void resetRezident(){
        if (country.get() != null){
            isRezident.set(country.get().rezidentCountryProperty().get());
        }
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
        countryCode.unbind();
        if (country.get() != null) {
            countryCode.bind(country.get().codeProperty().concat("   ").concat(country.get().descripProperty()));
        }
    }
    
    // DBService methods:
    public static List<Client> getAllFromDB() {
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().build()).toString();
            
            System.out.println("client data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static List<ClientStatus> getAllStatusFromDB(){
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
            String data = dbClient.select(DB_STATUS_TABLE, condition.build()).toString();
            
            System.out.println("client status data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<ClientStatus>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static List<Client> getFilteredFromDB(FilterModel model) { // JSONObject filter
        ClientFilterModel clientFilterModel = (ClientFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                                                .and("created_date", ">=", clientFilterModel.getFromDateForDB())
                                                .and("created_date", "<=", clientFilterModel.getToDateForDB());
        if (!clientFilterModel.isJuridicalIndeterminate()) {
            whereBuilder.and("is_jur", "=", clientFilterModel.isJuridicalSelected() ? 1 : 0);
        }
        if (!clientFilterModel.isRezidentIndeterminate()) {
            System.out.println("rezideeeeent: " + clientFilterModel.isRezidentSelected());
            whereBuilder.and("is_rezident", "=", clientFilterModel.isRezidentSelected() ? 1 : 0);
        }
        if (!clientFilterModel.isSelectedCountryALL()){
            whereBuilder.and("country_code", "=", clientFilterModel.getSelectedCountry().getCode());
        }
        if (clientFilterModel.hasSelectedStatuses()){
            whereBuilder = whereBuilder.andGroup();
            for (ClientStatus clientStatus : clientFilterModel.getSelectedStatuses()) {
                whereBuilder.or("status", "=", clientStatus.getClientStatusId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        
        try {
            JSONObject params = whereBuilder.condition().build();
            System.out.println("client filter sent params: " + params);
            
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
            JSONArray clientResult = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().where().and("rec_id", "=", id).condition().build());
            String data = clientResult.optJSONObject(0).toString();
            
            System.out.println("one client data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            Client client = mapper.readValue(data, Client.class);
            return client;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Client saveOneToDB(Client client) {
        if (client == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            JSONObject clientJson = new JSONObject(writer.writeValueAsString(client));
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newClient = dbClient.callProcedureAndGetAsJson("general_insert_update", DB_TABLE_NAME, dbClient.getLang(), clientJson).getJSONObject(0);
            
            System.out.println("save client data: " + newClient.toString());
            
            return mapper.readValue(newClient.toString(), Client.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public StringProperty wwwProperty(){
        return www;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public ObjectProperty<ClientStatus> statusProperty(){
        return clientStatus;
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
        return clientStatus.get().getClientStatusId();
    }
    
    public String getStatusDescrip(){
        return clientStatus.get().getDescrip();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getCountryCode(){
        return country.get().getCode();
    }
    
    
    public void setStatus(int status) {
        this.clientStatus.get().setClientStatusId(status);
    }
    
    public void setStatusDescrip(String statusDescrip){
        this.clientStatus.get().setDescrip(statusDescrip);
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
        System.out.println("code: " + countryCode);
        this.country.get().setCode(countryCode);
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
    
    
    @Override
    public boolean compares(EditorPanelable backup){
        Client otherClient = (Client) backup;
        boolean fieldsCompareResult =   this.isJur.get() == otherClient.getIsJur() &&
                                        this.isRezident.get() == otherClient.getIsRezident() && 
                                        this.firstName.get().equals(otherClient.getFirstName()) &&
                                        this.lastName.get().equals(otherClient.getLastName()) &&
                                        this.email.get().equals(otherClient.getEmail())    &&
                                        this.address.get().equals(otherClient.getAddress()) &&
                                        this.zipCode.get().equals(otherClient.getZipCode()) &&
                                        this.city.get().equals(otherClient.getCity()) &&
                                        this.country.get().equals(otherClient.countryProperty().get()) &&
                                        this.IDNumber.get().equals(otherClient.getIDNumber()) &&
                                        this.fax.get().equals(otherClient.getFax()) &&
                                        this.www.get().equals(otherClient.getWww()) &&
                                        this.clientStatus.get().equals(otherClient.statusProperty().get()) &&
                                        this.getRemark().equals(otherClient.getRemark()) &&
                                        Utils.avoidNullAndReturnString(this.createdDate).equals(Utils.avoidNullAndReturnString(otherClient.createdDate));
        boolean equalsPhones = Phone.compareLists(phones, otherClient.getPhones());
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
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        country.set(other.countryProperty().get());
        setIDNumber(other.getIDNumber());
        getPhones().setAll(other.getPhones()
                .stream()
                .map((Phone t) -> new Phone(t.getRecId(), t.getNumber()))
                .collect(Collectors.toList())
        );
        setFax(other.getFax());
        setWww(other.getWww());
        clientStatus.set(other.statusProperty().get());
        setRemark(other.getRemark());
        this.createdDate = other.createdDate;
    }

    @Override
    public String toStringForSearch(){
        String clientPhones = this.phones.stream()
                                        .map((phoneNumber) -> phoneNumber.getNumber() + " ")
                                        .reduce("", String::concat);

        String otherFieldsText = firstName.concat(" " + lastName.get())
                                          .concat(" " + email.get()).concat(" " + address.get())
                                          .concat(" " + city.get())
                                 .get();
        return (otherFieldsText + clientPhones);
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
                        view.setImage(new Image(isFirm ? IMAGE_OFFICE_URL : IMAGE_PERSON_URL));
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
