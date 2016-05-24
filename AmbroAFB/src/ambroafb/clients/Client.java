/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

/**
 *
 * @author mambroladze
 */

public class Client extends EditorPanelable{

    // ამ ველებს ჯერჯერობით არსად არ ვიყენებთ მაგრამ json-ში მოდის და ერორი რო არ ამოაგდოს მაგიტო საჭიროა რომ არსებობდნენ
    public String password, payPal, www;

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
    private final ObservableList<PhoneNumber> phoneList;

    @AView.Column(title = "%fax", width = "80")
    private final SimpleStringProperty fax;

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
        country.set(new Country("GE", "Georgia"));
        countryDescrip =    new SimpleStringProperty("");
        IDNumber =          new SimpleStringProperty("");
        phoneList = FXCollections.observableArrayList();
        phoneNumbers = new SimpleStringProperty("");
        fax = new SimpleStringProperty("");

        phoneList.addListener((ListChangeListener.Change<? extends PhoneNumber> c) -> {
            rebindPhoneNumbers();
        });
        rebindPhoneNumbers();

        country.addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            rebindCountry();
        });
        rebindCountry();
    }
    
    @Override
    public Client cloneWithoutID() {
        Client clone = new Client();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Client cloneWithID() {
        Client clone = cloneWithoutID();
        clone.recId = recId;
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
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        setCountry(other.getCountry());
        setIDNumber(other.getIDNumber());
        getPhoneList().setAll(
                other.getPhoneList()
                .stream()
                .map((PhoneNumber t) -> new PhoneNumber(t.getRecId(), t.getNumber()))
                .collect(Collectors.toList())
        );
        setFax(other.getFax());
    }

    private void rebindPhoneNumbers() {
        phoneNumbers.unbind();
        phoneNumbers.bind(phoneList
                .stream()
                .map(PhoneNumber::numberProperty)
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
            countryDescrip.bind(country.get().codeProperty().concat("   ").concat(country.get().nameProperty()));
        }
    }

    @Override
    public String toString() {
        return descrip.get() + " : " + email.get() + " : " + fullAddress.get();
    }

    public static List<Client> getClients() {
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("clients");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Client>>() {
            });
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public static Client getOneFromDB(int id) {
        System.out.println("called: getOneFromDB(" + id +")");
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("clients/" + id);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Client.class);
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return null;
    }

    public static Client saveOneToDB(Client client) {
        if (client == null) return null; 
        try {
            System.out.println("called: deleteOneFromDB(client), client = " + client.toString());
            String resource = "clients" + (client.recId > 0 ? "/" + client.recId : "");
            String method = client.recId > 0 ? "PUT" : "POST";
            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String client_str = mapper.writeValueAsString(client);
            
            System.out.println(Thread.currentThread().getStackTrace()[1].getClassName() + " .saveOneToDB() client_str: " + client_str);

            String res_str = GeneralConfig.getInstance().getServerClient().call(resource, method, client_str);
            Client res = mapper.readValue(res_str, Client.class);
            client.copyFrom(res);
            return client;
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return null;
    }

    public static boolean deleteOneFromDB(int id) {
        System.out.println("called: deleteOneFromDB(" + id +")");
        try {
            GeneralConfig.getInstance().getServerClient().call("clients/" + id, "DELETE", null);
            return true;
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return false;
    }

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

    public Country getCountry() {
        return country.get();
    }

    public ObservableList<PhoneNumber> getPhoneList() {
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

    public final void setPhoneList(Collection<PhoneNumber> phoneList) {
        this.phoneList.setAll(phoneList);
    }

    public final void setFax(String fax) {
        this.fax.set(fax);
    }
    
    @Override
    public String getFilterFieldValues(){
        String phones = "";
        phones = phoneList.stream().map((phoneNumber) -> phoneNumber.getNumber() + " ").reduce(phones, String::concat);

        String result = firstName.concat(" " + lastName.get())
                                .concat(" " + email.get()).concat(" " + address.get())
                                .concat(" " + zipCode.get()).concat(" " + city.get())
                                .concat(" " + country.getName()).concat(" " + fax.get())
                                .concat(" " + IDNumber.get())
                        .get();
        return result + phones;
    }
    
    public boolean equals(Client other){
        boolean fieldsCompareResult =   this.isJur.get() == other.getIsJur() &&
                                        this.isRez.get() == other.getIsRez() && 
                                        this.firstName.get().equals(other.getFirstName()) &&
                                        this.lastName.get().equals(other.getLastName()) &&
                                        this.email.get().equals(other.getEmail())    &&
                                        this.address.get().equals(other.getAddress()) &&
                                        this.zipCode.get().equals(other.getZipCode()) &&
                                        this.city.get().equals(other.getCity()) &&
                                        this.country.get().equals(other.getCountry()) &&
                                        this.IDNumber.get().equals(other.getIDNumber()) &&
                                        this.fax.get().equals(other.getFax());
        boolean equalsPhone = comparePhones(this.phoneList, other.getPhoneList());
        return fieldsCompareResult && equalsPhone;
    }
    
    private void printPhones(List<PhoneNumber> list){
        for (PhoneNumber phone : list){
            System.out.println("phone: " + phone);
        }
    }
    
    /**
     * The method compares clients phones list and pays attention size and order of them.
     * @param phoneList
     * @param otherPhoneList
     * @return 
     */
    public boolean comparePhones(ObservableList<PhoneNumber> phoneList, ObservableList<PhoneNumber> otherPhoneList) {
        if (phoneList.size() != otherPhoneList.size()) return false;

        for(int i = 0; i < phoneList.size(); i++){
            PhoneNumber phone = phoneList.get(i);
            PhoneNumber otherPhone = otherPhoneList.get(i);
            if ( !phone.getNumber().equals(otherPhone.getNumber()) ) return false;
        }

        return true;
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
