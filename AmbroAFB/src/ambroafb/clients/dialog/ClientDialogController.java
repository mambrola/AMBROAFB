/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.Editable;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.ListEditor;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.OkayCancel;
import ambroafb.general.okay_cancel.OkayCancelController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private CheckBox juridical;
    @FXML
    private Label first_name;
    @FXML
    private TextField firstName;
    @FXML
    private Label last_name;
    @FXML
    private TextField lastName;
    @FXML
    private TextField idNumber;
    @FXML
    private TextField email;
    @FXML
    private ListEditor<PhoneNumber> phone;
    @FXML
    private TextField fax;
    @FXML
    private TextField address;
    @FXML
    private TextField zipCode;
    @FXML
    private TextField city;
    @FXML
    private ComboBox<Country> country;
    @FXML
    private CheckBox rezident;
    @FXML
    private OkayCancelController okayCancelController;

    ArrayList<Node> focusTraversableNodes;
    private final GeneralConfig conf = GeneralConfig.getInstance();
    private boolean changeComponentValue;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        country.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country object) {
                return object.getCode() + "   " + object.getName();
            }

            @Override
            public Country fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        try {
            country.getItems().addAll(Country.getCountries());
        } catch (KFZClient.KFZServerException | IOException ex) {
            Logger.getLogger(ClientDialogController.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.WARNING, ex, "Can't load countries").showAlert();
        }
        
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        
        phone.setConverter(new StringConverter<PhoneNumber>() {
            @Override
            public String toString(PhoneNumber object) {
                return object != null ? object.getNumber() : null;
            }

            @Override
            public PhoneNumber fromString(String string) {
                return new PhoneNumber(string);
            }
        });
        
        juridical.setOnAction(this::switchJuridical);
        
    }
    
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
    

    public void bindClient(Client client) {
        if (client != null) {
            ValueChangeListener listener = new ValueChangeListener();
            
            juridical.selectedProperty().bindBidirectional(client.isJurProperty());
            
            rezident.selectedProperty().bindBidirectional(client.isRezProperty());
            rezident.selectedProperty().addListener(listener);
            
            firstName.textProperty().bindBidirectional(client.firstNameProperty());
            firstName.textProperty().addListener(listener);
            
            lastName.textProperty().bindBidirectional(client.lastNameProperty());
            lastName.textProperty().addListener(listener);
            
            idNumber.textProperty().bindBidirectional(client.IDNumberProperty());
            idNumber.textProperty().addListener(listener);
            
            email.textProperty().bindBidirectional(client.emailProperty());
            email.textProperty().addListener(listener);
            
            fax.textProperty().bindBidirectional(client.faxProperty());
            fax.textProperty().addListener(listener);
            
            address.textProperty().bindBidirectional(client.addressProperty());
            address.textProperty().addListener(listener);
            
            zipCode.textProperty().bindBidirectional(client.zipCodeProperty());
            zipCode.textProperty().addListener(listener);
            
            city.textProperty().bindBidirectional(client.cityProperty());
            city.textProperty().addListener(listener);
            
            country.valueProperty().bindBidirectional(client.countryProperty());
            country.valueProperty().addListener(listener);
            
            phone.setItems(client.getPhoneList());
        }
    }
    
    public boolean anyFieldWillChange(){
        return changeComponentValue;
    }
    
    public boolean allowToMakeOperation(){
        return okayCancelController.allowToMakeOperation();
    }
    
//    public void specificClose(){
//        okayCancelController.showAlertAndCheckClickForClose();
//    }
    

    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setConfirmationShowBy(buttonType);
        okayCancelController.changeOkayButtonTitleFor(buttonType);
        okayCancelController.makeButtonsVisibleBy(buttonType);
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
        phone.setEditable(false);
    }
    
    private class ValueChangeListener implements ChangeListener<Object> {

        @Override
        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            if ( !newValue.equals(oldValue) ){
                changeComponentValue = true;
            }
        }
        
    }
}
