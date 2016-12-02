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
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.GeneralConfig;
import ambroafb.phones.Phone;
import ambroafb.general.Utils;
import ambroafb.general.image_gallery.ImageGalleryController;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

@SuppressWarnings("EqualsAndHashcode")
public class Client extends EditorPanelable{

    // ამ ველებს ჯერჯერობით არსად არ ვიყენებთ მაგრამ json-ში მოდის და ერორი რო არ ამოაგდოს მაგიტო საჭიროა რომ არსებობდნენ
    
    
    private final StringProperty createdDate;
    
    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
//    private final SimpleIntegerProperty isJur;
    private final SimpleBooleanProperty isJurBool;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
//    private final SimpleIntegerProperty isRezident;
    private final SimpleBooleanProperty isRezidentBool;

    private final SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = "152")
    @JsonIgnore
    private final StringExpression descrip;

    @AView.Column(title = "%id_number", width = "120")
    @JsonProperty("passNumber")
    private final SimpleStringProperty IDNumber;

    @AView.Column(title = "%email", width = "170")
    private final SimpleStringProperty email;

    @AView.Column(title = "%phones", width = "128")
    @JsonIgnore
    private final SimpleStringProperty phoneNumbers;

    private final ObservableList<Phone> phones;
    
    @AView.Column(title = "%client_status", width = "110", styleClass = "textCenter")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final StringProperty statusDescrip;
    @JsonIgnore
    private final ObjectProperty<ClientStatus> clientStatus;
    private final IntegerProperty status;
    
    private final SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "250")
    @JsonIgnore
    private final StringExpression fullAddress;

    @AView.Column(title = "%country", width = "50", styleClass = "textCenter")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final SimpleStringProperty countryCode;
    @JsonIgnore
    private final ObjectProperty<Country> country;

    @AView.Column(title = "%fax", width = "80")
    private final SimpleStringProperty fax;
    
    @AView.Column(title = "%www_address", width = "128")
    private final SimpleStringProperty www;
    
    private final SimpleStringProperty remark;

    private final ObservableList<Document> documents;
    @JsonIgnore
    private ImageGalleryController clientImageGallery;
    
    @JsonIgnore
    private static final String DB_TABLE_NAME = "clients", DB_VIEW_NAME = "clients_whole", DB_STATUS_TABLE = "client_status_descrips";;
    
    @JsonIgnore
    private static final String IMAGE_OFFICE_URL = "/images/office.png", IMAGE_PERSON_URL = "/images/person.png";
    
    
    // Every property object has default values because of avoide NullpointerException in compares or any other methods in any case.
    public Client() {
        createdDate = new SimpleStringProperty("");
//        isJur =             new SimpleIntegerProperty();
        isJurBool = new SimpleBooleanProperty();
//        isRezident =        new SimpleIntegerProperty();
        isRezidentBool = new SimpleBooleanProperty();
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
        clientStatus = new SimpleObjectProperty(new ClientStatus());
        status = new SimpleIntegerProperty(0);
        statusDescrip = new SimpleStringProperty("");
        www = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        documents = FXCollections.observableArrayList();

        phones.addListener((ListChangeListener.Change<? extends Phone> c) -> {
            rebindPhoneNumbers();
        });
//        rebindPhoneNumbers(); // not needed. setPhones(..) methods and above list listener provides phonesNumbers changing.

        country.addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            rebindCountryCode();
//            resetRezident();
        });
        rebindCountryCode(); // country objectProperty already set country object. So this line is needed to change countryCode column when generate tableView Components.
        
        clientStatus.addListener((ObservableValue<? extends ClientStatus> observable, ClientStatus oldValue, ClientStatus newValue) -> {
            rebindStatus();
        });
        rebindStatus();
    }
    
//    private void resetRezident(){
//        if (country.get() != null){
//            isRezident.set(country.get().rezidentCountryProperty().get());
//        }
//    }
    
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

    private void rebindCountryCode() {
        countryCode.unbind();
        if (country.get() != null) {
            countryCode.bind(country.get().codeProperty());
        }
    }
    
    private void rebindStatus(){
        status.unbind();
        statusDescrip.unbind();
        if (clientStatus.get() != null){
            status.bind(clientStatus.get().clientStatusIdProperty());
            statusDescrip.bind(clientStatus.get().statusDescripProperty());
        }
    }
    
    
    // DBService methods:
    public static List<Client> getAllFromDB() {
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Client.class, DB_TABLE_NAME, params);
    }
    
    public static List<ClientStatus> getAllStatusFromDB(){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return DBUtils.getObjectsListFromDB(ClientStatus.class, DB_STATUS_TABLE, params);
    }
    
    public static List<Client> getFilteredFromDB(FilterModel model) { // JSONObject filter
        final ClientFilterModel clientFilterModel = (ClientFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                                                .and("created_date", ">=", clientFilterModel.getFromDateForDB())
                                                .and("created_date", "<=", clientFilterModel.getToDateForDB());
        if (!clientFilterModel.isJuridicalIndeterminate()) {
            whereBuilder.and("is_jur", "=", clientFilterModel.isJuridicalSelected() ? 1 : 0);
        }
        if (!clientFilterModel.isRezidentIndeterminate()) {
            whereBuilder.and("is_rezident", "=", clientFilterModel.isRezidentSelected() ? 1 : 0);
        }
        if (clientFilterModel.isSelectedConcreteCountry()){
            whereBuilder.and("country_code", "=", clientFilterModel.getSelectedCountry().getCode());
        }
        if (clientFilterModel.hasSelectedStatuses()){
            whereBuilder = whereBuilder.andGroup();
            for (ClientStatus clientStatus : clientFilterModel.getSelectedStatuses()) {
                whereBuilder.or("status", "=", clientStatus.getClientStatusId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
//        whereBuilder.and("email", "is not ", "null"); //  only clients, not partners
        
        JSONObject params = whereBuilder.condition().build();
        System.out.println("filter params: " + params);
        ArrayList<Client> clientsFromDB = DBUtils.getObjectsListFromDB(Client.class, DB_VIEW_NAME, params);
        if (clientFilterModel.isTypeIndeterminate()){
            return clientsFromDB;
        }
        // else filter only clients or only partners:
        return clientsFromDB.stream().filter((Client c) -> {
            boolean partner = c.getEmail() == null || c.getEmail().isEmpty();
            return (clientFilterModel.isTypeSelected()) ? !partner : partner;
        }).collect(Collectors.toList());
    }
    
    public static Client getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Client.class, DB_VIEW_NAME, params);
    }

    public static Client saveOneToDB(Client client) {
        if (client == null) return null;
        Client clientFromDB = DBUtils.saveObjectToDB(client, "client");
        System.out.println("client: " + clientFromDB.getRecId());
        client.getClientImageGallery().sendDataToServer("" + clientFromDB.getRecId());
        return clientFromDB;
    }

    public static boolean deleteOneFromDB(int id) {
        return DBUtils.deleteObjectFromDB("client_delete", id);
    }

    
    //Properties getters:
    public SimpleBooleanProperty isJurProperty() {
        return isJurBool;
    }

    public SimpleBooleanProperty isRezProperty() {
        return isRezidentBool;
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

    public StringProperty phoneNumbersProperty() {
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
//    @JsonIgnore
//    public String getCreatedDate(){
//        return createdDate.get();
//    }
    
    @JsonIgnore
    public LocalDate getCreatedDateAsObj(){
        return DateConverter.getInstance().parseDate(createdDate.get());
    }
    
    public boolean getIsJur() {
        return isJurBool.get();
    }

    public boolean getIsRezident() {
        return isRezidentBool.get();
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
    
    // for sending: DB json need key name 'descrip' statusDescrip
    // for receiving: json contains key name 'statusDescrip', so we need setStatusDescrip method.
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getDescrip(){
        return clientStatus.get().getDescrip();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getCountryCode(){
        return country.get().getCode();
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
        this.country.get().setCode(countryCode);
    }
    
    public ObservableList<Document> getDocuments(){
        return documents;
    }
    
    @JsonIgnore
    public ImageGalleryController getClientImageGallery(){
        return clientImageGallery;
    }
    
    /**
     * The method create short description of client by firstName, lastName and email.
     * @param delimiter The sign between of client full name and email.
     * @return The expression of client short information.
     */
    @JsonIgnore
    public StringExpression getShortDescrip(String delimiter){
        return  Utils.avoidNull(firstName).concat("  ")
                    .concat(Utils.avoidNull(lastName)).concat(delimiter)
                    .concat(Utils.avoidNull(email));
    }
    

     // Setters:
    private void setCreatedDate(String date){
        this.createdDate.set(date);
    }
    
    public final void setIsJur(int isJur) {
        this.isJurBool.set(isJur == 1);
    }

    public final void setIsRezident(int isRez) {
        this.isRezidentBool.set(isRez == 1);
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
    
    public void setStatus(int status) {
        this.clientStatus.get().setClientStatusId(status);
    }
    
    public void setStatusDescrip(String statusDescrip){
        this.clientStatus.get().setDescrip(statusDescrip);
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
    
    public void setDocuments(Collection<Document> documents){
        this.documents.setAll(documents);
    }
    
    public void setClientImageGallery(ImageGalleryController imageGallery){
        this.clientImageGallery = imageGallery;
    }
    
    
    @Override
    public boolean compares(EditorPanelable backup){
        Client otherClient = (Client) backup;
        boolean fieldsCompareResult =   getIsJur() == otherClient.getIsJur() &&
                                        getIsRezident() == otherClient.getIsRezident() && 
                                        getFirstName().equals(otherClient.getFirstName()) &&
                                        getLastName().equals(otherClient.getLastName()) &&
                                        getEmail().equals(otherClient.getEmail())    &&
                                        getAddress().equals(otherClient.getAddress()) &&
                                        getZipCode().equals(otherClient.getZipCode()) &&
                                        getCity().equals(otherClient.getCity()) &&
                                        getCountryCode().equals(otherClient.getCountryCode()) &&
                                        getIDNumber().equals(otherClient.getIDNumber()) &&
                                        getFax().equals(otherClient.getFax()) &&
                                        getWww().equals(otherClient.getWww()) &&
                                        statusProperty().get().compares(otherClient.statusProperty().get()) &&
                                        getRemark().equals(otherClient.getRemark());
//                                        getCreatedDate().equals(otherClient.getCreatedDate());
        boolean equalsPhones = Utils.compareListsByElemOrder(phones, otherClient.getPhones());
        return fieldsCompareResult && equalsPhones;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Client otherClient = (Client) other;
        return  getRecId() == otherClient.getRecId() ||
                getShortDescrip("").equals(otherClient.getShortDescrip(""));
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
//        setCreatedDate(other.getCreatedDate());
        setIsJur(Utils.getIntFromBoolean(other.getIsJur()));
        setIsRezident(Utils.getIntFromBoolean(other.getIsRezident()));
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setEmail(other.getEmail());
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        setCountryCode(other.getCountryCode());
        setIDNumber(other.getIDNumber());
        getPhones().clear(); // Avoid to add twise phones in tableView
        getPhones().addAll(other.getPhones()
                .stream()
                .map((Phone t) -> new Phone(t.getRecId(), t.getNumber()))
                .collect(Collectors.toList())
        );
        setFax(other.getFax());
        setWww(other.getWww());
        clientStatus.get().copyFrom(other.statusProperty().get());
        setRemark(other.getRemark());
    }

    @Override
    public String toStringForSearch(){
        String clientPhones = getPhones().stream()
                                        .map((phoneNumber) -> phoneNumber.getNumber() + " ")
                                        .reduce("", String::concat);

        String otherFieldsText = getFirstName() + " " + getLastName() + " " + 
                                 getEmail() + " " + getAddress() + " " + getCity();
                
        return (otherFieldsText + " " + clientPhones);
    }
    
    
    @Override
    public String toString(){
        return getShortDescrip("").get();
    }
    
    public static class Document {
        public String path;
        public int recId;
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
