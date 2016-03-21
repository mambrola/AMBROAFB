/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class ClientDialogController implements Initializable {

    GeneralConfig conf = GeneralConfig.getInstance();
    ArrayList<Node> focusTraversableNodes;
    Client client;

    private Consumer<Client> onCreate;
    private Consumer<Void> onCancell;

    @FXML
    VBox formPane;
    @FXML
    private Label first_name, last_name;
    @FXML
    DatePicker openDate;
    @FXML
    CheckBox juridical, rezident;
    @FXML
    TextField firstName, lastName, idNumber, email, fax, address, zipCode, city;
    @FXML
    ComboBox country, phone;

    @FXML
    private void switchJuridical(ActionEvent e) {
        System.out.println("e.getSource(): " + firstName.widthProperty().getValue());
        double w = firstName.widthProperty().getValue() + lastName.widthProperty().getValue();
        if (((CheckBox) e.getSource()).isSelected()) {
            first_name.setText(conf.getTitleFor("firm_name"));
            last_name.setText(conf.getTitleFor("firm_form"));
            firstName.setPrefWidth(0.75 * w);
            lastName.setPrefWidth(0.25 * w);
        } else {
            first_name.setText(conf.getTitleFor("first_name"));
            last_name.setText(conf.getTitleFor("last_name"));
            firstName.setPrefWidth(0.50 * w);
            lastName.setPrefWidth(0.50 * w);
        }
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Country.dbGetCountries("").values().stream().forEach((c) -> {
            country.getItems().add(c.getFullDescrip());
        });
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
    }

    @FXML
    private void saveClient() {
        System.out.println("method 'saveClient'");
        if (onCreate != null) {
            if (client == null) {
                client = new Client();
            }
            client.setIsJur(juridical.isSelected());
            client.setIsRez(rezident.isSelected());
            client.setFirstName(firstName.getText());
            client.setLastName(lastName.getText());
            client.setEmail(email.getText());
            client.setAddress(address.getText());
            client.setZipCode(zipCode.getText());
            client.setCity(city.getText());
            client.setCountry((String) country.getValue());
            client.setIDNumber(idNumber.getText());
            client.setPhones(StringUtils.join(phone.getItems(), ":;:"));
            client.setFax(fax.getText());
            onCreate.accept(client);
        }
    }

    @FXML
    private void cancel() {

        System.out.println("CCCCCCCCCCCCCCCCCCanceled");
//        if (onCancell != null) {
//            onCancell.accept(null);
//        }
    }

    @FXML
    private void okay() {
        System.out.println("OOOOOOOOOOOOOOOOOOkaied");
    }

    public void onCreate(Consumer<Client> callback) {
        onCreate = callback;
    }

    public void onCancell(Consumer<Void> callback) {
        onCancell = callback;
    }

    public void setDisabled() {
        focusTraversableNodes.forEach((Node t) -> {
            if (t != phone) {
                t.setDisable(true);
            }
        });
        phone.setEditable(false);
    }

    public void setClient(Client client) {
        if (client != null) {
            juridical.setSelected(client.getIsJur());
            rezident.setSelected(client.getIsRez());
            firstName.setText(client.getFirstName());
            lastName.setText(client.getLastName());
            idNumber.setText(client.getIDNumber());
            email.setText(client.getEmail());
            fax.setText(client.getFax());
            address.setText(client.getAddress());
            zipCode.setText(client.getZipCode());
            city.setText(client.getCity());
            country.setValue(client.getCountry());
            phone.getItems().setAll(client.getPhoneList());
        }
        this.client = client;
    }

}
