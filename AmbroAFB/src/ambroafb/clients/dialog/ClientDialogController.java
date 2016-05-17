/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.GeneralConfig;
import ambroafb.general.ListEditor;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import ambroafb.general.country_combobox.CountryComboBox;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.OkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientDialogController implements Initializable {
    @FXML
    private VBox formPane;
    @FXML
    private DatePicker openDate;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private Label first_name, last_name;
    @FXML
    private TextField firstName, lastName, idNumber, email, fax, address, zipCode, city;
    @FXML
    private ListEditor<PhoneNumber> phone;
//    @FXML
//    private ComboBox<Country> country;
    @FXML
    private CountryComboBox country;
    @FXML
    private OkayCancelController okayCancelController;

    private ArrayList<Node> focusTraversableNodes;
    private final GeneralConfig conf = GeneralConfig.getInstance();
    private Client client;
    private Client clientBackup;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        country.setConverter(new StringConverter<Country>() {
//            @Override
//            public String toString(Country object) {
//                return object.getCode() + "   " + object.getName();
//            }
//
//            @Override
//            public Country fromString(String string) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        country.getItems().addAll(Country.getAllFromDB());
//        country = new CountryComboBox();
        
        country.getItems().addAll(Country.getAllFromDB());
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        
//        phone.setConverter(new StringConverter<PhoneNumber>() {
//            @Override
//            public String toString(PhoneNumber object) {
//                return object != null ? object.getNumber() : null;
//            }
//
//            @Override
//            public PhoneNumber fromString(String string) {
//                return new PhoneNumber(string);
//            }
//        });
        
        juridical.setOnAction(this::switchJuridical);
        
    }

    public void bindClient(Client client) {
        this.client = client;
        if (client != null) {
            juridical.selectedProperty().bindBidirectional(client.isJurProperty());
            rezident.selectedProperty().bindBidirectional(client.isRezProperty());
            firstName.textProperty().bindBidirectional(client.firstNameProperty());
            lastName.textProperty().bindBidirectional(client.lastNameProperty());
            idNumber.textProperty().bindBidirectional(client.IDNumberProperty());
            email.textProperty().bindBidirectional(client.emailProperty());
            fax.textProperty().bindBidirectional(client.faxProperty());
            address.textProperty().bindBidirectional(client.addressProperty());
            zipCode.textProperty().bindBidirectional(client.zipCodeProperty());
            city.textProperty().bindBidirectional(client.cityProperty());
            country.valueProperty().bindBidirectional(client.countryProperty());
            phone.setItems(client.getPhoneList());
        }
    }
    
    public void setBackupClient(Client backupClient){
        this.clientBackup = backupClient;
    }
    
    public boolean anyFieldChanged(){
        return !client.equals(clientBackup);
    }
    
    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void switchJuridical(ActionEvent e) {
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
     * Disables all fields on Dialog stage except phones.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            if (t != phone) {
                t.setDisable(true);
            }
        });
//        phone.setEditable(false);
    }

    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }

    public OkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
