/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.general.Editable;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
public final class Client {

    // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    public int clientId;

    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private SimpleBooleanProperty isJur;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
    private SimpleBooleanProperty isRez;

    private SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = "152")
    private StringExpression descrip;

    @AView.Column(title = "%email", width = "170")
    private SimpleStringProperty email;

    private SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "270")
    private StringExpression fullAddress;

    private ObjectProperty<Country> country;

//    private SimpleStringProperty countryCode;
    @AView.Column(title = "%country", width = "80")
    private SimpleStringProperty countryDescrip;

    @AView.Column(title = "%id_number", width = "100")
    private SimpleStringProperty IDNumber;

    @AView.Column(title = "%phones", width = "300")
    private SimpleStringProperty phoneNumbers;

    private ObservableList<PhoneNumber> phoneList;

    @AView.Column(title = "%fax", width = "80")
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
        country = new SimpleObjectProperty<>();
        countryDescrip = new SimpleStringProperty();
        IDNumber = new SimpleStringProperty();
        phoneList = FXCollections.observableArrayList();
        phoneNumbers = new SimpleStringProperty();
        fax = new SimpleStringProperty();

        phoneList.addListener((ListChangeListener.Change<? extends PhoneNumber> c) -> {
            rebindPhoneNumbers();
        });
        rebindPhoneNumbers();

        country.addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            rebindCountry();
        });
        rebindCountry();
    }

    public Client(Object[] values) {
        this();
        System.out.println("values: " + Utils.avoidNullAndReturnBoolean(values[1]) + ":" + values[1]);
        clientId = Utils.avoidNullAndReturnInt(values[0]);
        setIsJur(Utils.avoidNullAndReturnBoolean(values[1]));
        setIsRez(Utils.avoidNullAndReturnBoolean(values[2]));
        setFirstName(Utils.avoidNullAndReturnString(values[3]));
        setLastName(Utils.avoidNullAndReturnString(values[4]));
        setEmail(Utils.avoidNullAndReturnString(values[5]));
        setAddress(Utils.avoidNullAndReturnString(values[6]));
        setZipCode(Utils.avoidNullAndReturnString(values[7]));
        setCity(Utils.avoidNullAndReturnString(values[8]));
        setCountry((Country) values[9]);
        setIDNumber(Utils.avoidNullAndReturnString(values[10]));
        getPhoneList().setAll((Collection<PhoneNumber>) values[11]);
        setFax(Utils.avoidNullAndReturnString(values[12]));
    }

    public Client(JSONObject obj) throws JSONException {
        this();
        clientId = obj.getInt("recId");
        setIsJur(obj.optBoolean("isJur"));
        setIsRez(obj.optBoolean("isRez"));
        setFirstName(obj.optString("firstName"));
        setLastName(obj.optString("lastName"));
        setEmail(obj.optString("email"));
        setAddress(obj.optString("address"));
        setZipCode(obj.optString("zipCode"));
        setCity(obj.optString("city"));
        setCountry(new Country(obj.optJSONObject("country")));
        setIDNumber(obj.optString("passNumber"));
        getPhoneList().setAll(PhoneNumber.jsonToPhoneList(obj.optJSONArray("phoneNumbers")));
        setFax(obj.optString("fax"));
    }

    public Client cloneWithoutID() {
        Client clone = new Client();
        clone.copyFrom(this);

        return clone;
    }

    public Client cloneWithID() {
        Client clone = cloneWithoutID();
        clone.clientId = clientId;
        return clone;
    }

    public void copyFrom(Client other) {
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
                .map((PhoneNumber t) -> new PhoneNumber(t.getId(), t.getNumber()))
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

    public Client dbGetClient(int clientId) {
        return dbGetClients(clientId).get(clientId);
    }

    static List<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:8080/KFZ_Server/api/clients");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                JSONArray arr = new JSONArray(Utils.readStream(con.getInputStream()));
                for (int i = 0; i < arr.length(); i++) {
                    clients.add(new Client(arr.getJSONObject(i)));
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }

    static HashMap<Integer, Client> dbGetClients(int recId) {
        HashMap<Integer, Client> clients = new HashMap();
        String query = "SELECT * FROM clients_to_java" + (recId == 0 ? "" : " where rec_id = " + Integer.toString(recId)) + " ORDER BY rec_id";
        String[] orderedRequestedFields = new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country", "pass_number", "phones", "fax"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {

            System.out.println("row: " + row[12]);

            row[11] = dbStringToPhones(row[11] == null ? "" : row[11].toString());
            row[9] = dbStringToCountry(row[9] == null ? "" : row[9].toString());

            clients.put((int) row[0], new Client(row));
        });
        return clients;
    }

    private static Collection<PhoneNumber> dbStringToPhones(String phones) {
        ArrayList<PhoneNumber> list = new ArrayList<>();
        if (phones == null || phones.isEmpty()) {
            return list;
        }
        for (String phone : Arrays.asList(phones.split(":;:"))) {
            String ph[] = phone.split(";:;");
            list.add(new PhoneNumber(Integer.parseInt(ph[0]), ph[1]));
        }
        return list;
    }

    private static Country dbStringToCountry(String c) {
        if (c == null || c.isEmpty()) {
            return null;
        }
        String cntr[] = c.split(";:;");
        return new Country(cntr[0], cntr[1]);
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

    public final void setIDNumber(String IDNumber) {
        this.IDNumber.set(IDNumber);
    }

    public final void setPhoneList(ObservableList<PhoneNumber> phoneList) {
        this.phoneList = phoneList;
    }

    public final void setFax(String fax) {
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

    public static class PhoneNumber implements Editable<String> {

        public static List<PhoneNumber> jsonToPhoneList(JSONArray arr) throws JSONException {
            ArrayList<PhoneNumber> list = new ArrayList<>();
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    list.add(new PhoneNumber(arr.getJSONObject(i)));
                }
            }
            return list;
        }

        private int recId;
        private final StringProperty number = new SimpleStringProperty();

        public PhoneNumber() {
        }

        public PhoneNumber(JSONObject obj) throws JSONException {
            this(obj.getInt("recId"), obj.getString("number"));
        }

        public PhoneNumber(int id, String number) {
            this.recId = id;
            this.number.set(number);
        }

        public PhoneNumber(String number) {
            this.number.set(number);
        }

        public int getId() {
            return recId;
        }

        public void setId(int id) {
            this.recId = id;
        }

        public String getNumber() {
            return number.get();
        }

        public void setNumber(String value) {
            number.set(value);
        }

        public StringProperty numberProperty() {
            return number;
        }

        @Override
        public void edit(String param) {
            setNumber(param);
        }

        @Override
        public ObservableValue<String> getObservableString() {
            return number;
        }

        @Override
        public String toString() {
            return "PhoneNumber{" + "id=" + recId + ", number=" + number + '}';
        }

    }

    public static class Country {

        private int id;
        private final StringProperty code = new SimpleStringProperty();
        private final StringProperty name = new SimpleStringProperty();

        public Country() {
        }

        public Country(JSONObject obj) throws JSONException {
            this(obj.getString("countryCode"), obj.getString("descrip"));
        }

        public Country(int id, String code, String name) {
            this(code, name);
            this.id = id;
        }

        public Country(String code, String name) {
            this.code.set(code);
            this.name.set(name);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code.get();
        }

        public void setCode(String value) {
            code.set(value);
        }

        public StringProperty codeProperty() {
            return code;
        }

        public String getName() {
            return name.get();
        }

        public void setName(String value) {
            name.set(value);
        }

        public StringProperty nameProperty() {
            return name;
        }
    }
}
