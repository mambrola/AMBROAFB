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
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
    private SimpleBooleanProperty isRez;

    private SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = "120")
    private StringExpression descrip;

    @AView.Column(title = "%email", width = "150")
    private SimpleStringProperty email;

    private SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "200")
    private StringExpression fullAddress;

    private SimpleStringProperty country_code;

    @AView.Column(title = "%country", width = "70")
    private SimpleStringProperty country;

    @AView.Column(title = "%id_number", width = "100")
    private SimpleStringProperty IDNumber;

    private ArrayList<String> phoneList;

    @AView.Column(title = "%phone", width = "300")
    private SimpleStringProperty phones;

    @AView.Column(title = "%fax", width = "100")
    private SimpleStringProperty fax;

    public Client() {
        isJur = new SimpleBooleanProperty();
        isRez = new SimpleBooleanProperty();
        firstName = new SimpleStringProperty();
        lastName = new SimpleStringProperty();
        descrip = firstName.concat(" ").concat(lastName);
        email = new SimpleStringProperty();
        address = new SimpleStringProperty();
        zipCode = new SimpleStringProperty();
        city = new SimpleStringProperty();
        fullAddress = address.concat(", ").concat(zipCode).concat(", ").concat(city);
        country_code = new SimpleStringProperty();
        country = new SimpleStringProperty();
        IDNumber = new SimpleStringProperty();
        phoneList = new ArrayList<>();
        phones = new SimpleStringProperty();
        fax = new SimpleStringProperty();

        phones.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            phoneList.clear();
            if (newValue != null) {
                phoneList.addAll(Arrays.asList(newValue.split(", ")));
            }
        });
    }

    public Client(Object[] values) {
        this();
        System.out.println("values: " + Utils.avoidNullAndReturnBoolean(values[1]) + ":" + values[1]);
        clientId = Utils.avoidNullAndReturnInt(values[0]);
        isJur.set(Utils.avoidNullAndReturnBoolean(values[1]));
        isRez.set(Utils.avoidNullAndReturnBoolean(values[2]));
        firstName.set(Utils.avoidNullAndReturnString(values[3]));
        lastName.set(Utils.avoidNullAndReturnString(values[4]));
        email.set(Utils.avoidNullAndReturnString(values[5]));
        address.set(Utils.avoidNullAndReturnString(values[6]));
        zipCode.set(Utils.avoidNullAndReturnString(values[7]));
        city.set(Utils.avoidNullAndReturnString(values[8]));
        country_code.set(Utils.avoidNullAndReturnString(values[9]));
        country.set(Utils.avoidNullAndReturnString(values[10]));
        IDNumber.set(Utils.avoidNullAndReturnString(values[11]));
        phones.set(Utils.avoidNullAndReturnString(values[12]));
        fax.set(Utils.avoidNullAndReturnString(values[13]));
    }

    @Override
    public String toString() {
        return descrip.get() + " : " + email.get() + " : " + fullAddress.get();
    }

    public Client dbGetClient(int clientId) {
        return dbGetClients(clientId).get(clientId);
    }

    static HashMap<Integer, Client> dbGetClients(int recId) {
        HashMap<Integer, Client> clients = new HashMap();
        String query = "SELECT * FROM clients_to_java" + (recId == 0 ? "" : " where rec_id = " + Integer.toString(recId)) + " ORDER BY rec_id";
        String[] orderedRequestedFields = new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country_code", "country_descrip", "pass_number", "phones", "fax"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {

            System.out.println("row: " + row[12]);

            clients.put((int) row[0], new Client(row));
        });
        return clients;
    }

    public SimpleBooleanProperty isJurProperty() {
        return isJur;
    }

    public boolean getIsJur() {
        return isJur.get();
    }

    public void setIsJur(boolean isJur) {
        this.isJur.set(isJur);
    }

    public SimpleBooleanProperty isRezProperty() {
        return isRez;
    }

    public boolean getIsRez() {
        return isRez.get();
    }

    public void setIsRez(boolean isRez) {
        this.isRez.set(isRez);
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringExpression descripProperty() {
        return descrip;
    }

    public String getDescrip() {
        return descrip.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public SimpleStringProperty zipCodeProperty() {
        return zipCode;
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringExpression fullAddressProperty() {
        return fullAddress;
    }

    public String getFullAddress() {
        return fullAddress.get();
    }

//    public void setFullAddress(String fullAddress) {
//        this.fullAddress.set(fullAddress);
//    }
    public SimpleStringProperty country_codeProperty() {
        return country_code;
    }

    public String getCountry_code() {
        return country_code.get();
    }

    public void setCountry_code(String country_code) {
        this.country_code.set(country_code);
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public SimpleStringProperty IDNumberProperty() {
        return IDNumber;
    }

    public String getIDNumber() {
        return IDNumber.get();
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber.set(IDNumber);
    }

    public ArrayList<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<String> phoneList) {
        this.phoneList = phoneList;
    }

    public SimpleStringProperty phonesProperty() {
        return phones;
    }

    public String getPhones() {
        return phones.get();
    }

    public void setPhones(String phones) {
        this.phones.set(phones.replaceAll(":;:", ",  "));
    }

    public SimpleStringProperty faxProperty() {
        return fax;
    }

    public String getFax() {
        return fax.get();
    }

    public void setFax(String fax) {
        this.fax.set(fax);
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
