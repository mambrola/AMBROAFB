/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.countries.*;
import ambroafb.general.KFZClient;
import ambroafb.phones.PhoneComboBox;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ambroafb.general.interfaces.Annotations.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.controlsfx.control.textfield.TextFields;


/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientDialogController implements Initializable {
    @FXML
    private VBox formPane, phonesContainer;
    @FXML
    private ADatePicker openDate;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private Label first_name, last_name;
    
    @FXML  
    @ContentNotEmpty
    private TextField firstName, lastName, idNumber;
    
    @FXML 
    @ContentNotEmpty 
    @ContentMail
    private TextField email;
    
    @FXML  
    @ContentNotEmpty
    private TextField address; // this place must be because of required fields order.
    
    @FXML
    private TextField fax, zipCode, city;
    
    @FXML
    private CountryComboBox country;
    @FXML
    private DialogOkayCancelController okayCancelController;

    private ArrayList<Node> focusTraversableNodes;
    private final GeneralConfig conf = GeneralConfig.getInstance();
    private Client client;
    private Client clientBackup;
    private AutoCompletionBinding<String> chooseCityBinding;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        juridical.setOnAction(this::switchJuridical);
        Thread accessCities = new Thread(new BackgroundAccessToDB("/generic/cities"));
        accessCities.start();
    }

    public void bindClient(Client client) {
        this.client = client;
        if (client != null) {
            openDate.setValue(getClientCreatedDate());
            juridical.selectedProperty().bindBidirectional(client.isJurProperty());
            rezident. selectedProperty().bindBidirectional(client.isRezProperty());
            firstName.    textProperty().bindBidirectional(client.firstNameProperty());
            lastName.     textProperty().bindBidirectional(client.lastNameProperty());
            idNumber.     textProperty().bindBidirectional(client.IDNumberProperty());
            email.        textProperty().bindBidirectional(client.emailProperty());
            fax.          textProperty().bindBidirectional(client.faxProperty());
            address.      textProperty().bindBidirectional(client.addressProperty());
            zipCode.      textProperty().bindBidirectional(client.zipCodeProperty());
            city.         textProperty().bindBidirectional(client.cityProperty());
            country.     valueProperty().bindBidirectional(client.countryProperty());
        }
    }
    
    private LocalDate getClientCreatedDate(){
        LocalDate result = null;
        String date = client.createdDate;
        if (date != null){
            int beforeTime = date.indexOf(" ");
            String onlyDatePart = client.createdDate.substring(0, beforeTime);
            result = LocalDate.parse(onlyDatePart);
        }
        return result;
    }
    
    public void setBackupClient(Client backupClient){
        this.clientBackup = backupClient;
    }
    
    public boolean anyFieldChanged(){
        return !client.compares(clientBackup);
    }
    
    public void setNextVisibleAndActionParameters(EDITOR_BUTTON_TYPE buttonType) {
        openDate.setDisable(true);
        boolean editable = true;
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
            editable = false;
        }
        if (client != null){
            PhoneComboBox phonesCombobox = new PhoneComboBox(client.getPhoneList(), editable);
            phonesContainer.getChildren().add(phonesCombobox);
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
            t.setDisable(true);
        });
    }

    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    private class BackgroundAccessToDB implements Runnable {

        private String path;
        
        public BackgroundAccessToDB(String servicePath){
            path = servicePath;
        }
        
        @Override
        public void run() {
            try {
                JSONArray cities = new JSONArray(GeneralConfig.getInstance().getServerClient().get(path));
                List<String> citiesAsList = getListFromJSONArray(cities);
                chooseCityBinding = TextFields.bindAutoCompletion(
                                                        city,
                                                        (AutoCompletionBinding.ISuggestionRequest param) -> citiesAsList.stream().filter((cityName) ->
                                                            cityName.toLowerCase().contains(param.getUserText().toLowerCase()) )
                                                        .collect(Collectors.toList()), 
                                                        null);
            } catch (IOException | KFZClient.KFZServerException | JSONException ex) {
                Logger.getLogger(ClientDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private List<String> getListFromJSONArray(JSONArray cities) throws JSONException{
            List<String> result = new ArrayList<>();
            for (int i = 0; i < cities.length(); i++){
                String currCity = cities.getString(i).trim();
                if (!currCity.isEmpty())
                    result.add(cities.getString(i));
            }
            return result;
        }
    }
}
