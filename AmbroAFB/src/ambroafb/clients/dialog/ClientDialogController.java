/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.clients.helper.ClientStatusComboBox;
import ambroafb.countries.*;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.image_gallery.ImageGalleryController;
import ambroafb.general.interfaces.Annotations.ContentMail;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.phones.PhoneComboBox;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientDialogController extends DialogController {
    @FXML
    private VBox formPane, phonesContainer;
    @FXML
    private ADatePicker openDate;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private Label first_name, last_name;
    
    // start required nodes:
    @FXML @ContentNotEmpty(predicate = CustomPredicate.class)
    private TextField firstName, lastName, idNumber;
    
    @FXML
    @ContentNotEmpty(predicate = CustomPredicate.class)
    @ContentMail(predicate = CustomPredicate.class)
    private TextField email;
    
    @FXML @ContentNotEmpty(predicate = CustomPredicate.class)
    private ImageGalleryController imageGalleryController;
    
    @FXML  @ContentNotEmpty(predicate = CustomPredicate.class)
    private TextField address; // this place must be because of required fields order.
    
    @FXML @ContentNotEmpty(predicate = CustomPredicate.class)
    private CountryComboBox country;
    @FXML @ContentNotEmpty(predicate = CustomPredicate.class)
    private ClientStatusComboBox statuses;
    // end required nodes.
    
    
    @FXML
    private TextField fax, zipCode, city, www;
    @FXML
    private DialogOkayCancelController okayCancelController;
    @FXML
    private HBox namesRootPane;
    
    private final GeneralConfig conf = GeneralConfig.getInstance();
    
    private static final ObjectProperty<ClientStatus> statusProperty = new SimpleObjectProperty<>();
    
    private final String serviceURLPrefix = "/clients/passport/";
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        juridical.setOnAction(this::switchJuridical);
        Thread accessCities = new Thread(new BackgroundAccessToDB("/generic/cities"));
        accessCities.start();
        country.valueProperty().addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
            if (newValue != null && oldValue != null && !newValue.equals(oldValue)){
                rezident.setSelected(newValue.getDescrip().equals("Georgia"));
            }
        });
        country.fillComboBoxWithouyALL(null);
        statuses.fillComboBox(null);
//        statuses.getItems().setAll(Client.getAllStatusFromDB());
    }

    private void switchJuridical(ActionEvent e) {
        String delimiter = " ";
        if (((CheckBox) e.getSource()).isSelected()) {
            changeSceneVisualAsFirm(delimiter);
        } else {
            changeSceneVisualAsPerson(delimiter);
        }
    }
    
    private void changeSceneVisualAsFirm(String delimiter){
        first_name.setText(conf.getTitleFor("firm_name"));
            
        setStylesForNamesPaneElements("twoThirds", "coupleTwoThird", "coupleOneThird");
        namesRootPane.getChildren().remove(1); // remove lastName VBox

        String firmDescrip = firstName.getText() + delimiter + lastName.getText();
        firstName.setText(firmDescrip.trim());
        lastName.setText(null);
    }
    
    private void changeSceneVisualAsPerson(String delimiter){
        first_name.setText(conf.getTitleFor("first_name"));
        last_name.setText(conf.getTitleFor("last_name"));

        VBox lastNameVBox = new VBox(last_name, lastName);
        namesRootPane.getChildren().add(1, lastNameVBox);
        lastNameVBox.getStyleClass().add("couple");

        setStylesForNamesPaneElements("oneThirds", "couple", "couple");

        String firmDescrip = firstName.getText();
        String firstNameText = StringUtils.substringBeforeLast(firmDescrip, delimiter);
        String lastNameText = StringUtils.substringAfterLast(firmDescrip, delimiter);
        firstName.setText(firstNameText.trim());
        lastName.setText(lastNameText.trim());
    }
    
    private void setStylesForNamesPaneElements(String namesRootPaneNewStyleClass, String firstNameVBoxNewStyleClass, String idNumberVBoxNewStyleClass){
        namesRootPane.getStyleClass().clear();
        namesRootPane.getStyleClass().add(namesRootPaneNewStyleClass);
        
        ((VBox)firstName.getParent()).getStyleClass().clear();
        ((VBox)firstName.getParent()).getStyleClass().add(firstNameVBoxNewStyleClass);

        ((VBox)idNumber.getParent()).getStyleClass().clear();
        ((VBox)idNumber.getParent()).getStyleClass().add(idNumberVBoxNewStyleClass);
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null) {
            Client client = (Client)object;
            openDate.setValue(client.getCreatedDateObj());
            juridical.selectedProperty().bindBidirectional(client.isJurProperty());
            rezident. selectedProperty().bindBidirectional(client.isRezProperty());
            firstName.    textProperty().bindBidirectional(client.firstNameProperty());
            lastName.     textProperty().bindBidirectional(client.lastNameProperty());
            idNumber.     textProperty().bindBidirectional(client.IDNumberProperty());
            email.        textProperty().bindBidirectional(client.emailProperty());
            fax.          textProperty().bindBidirectional(client.faxProperty());
            www.          textProperty().bindBidirectional(client.wwwProperty());
            address.      textProperty().bindBidirectional(client.addressProperty());
            zipCode.      textProperty().bindBidirectional(client.zipCodeProperty());
            city.         textProperty().bindBidirectional(client.cityProperty());
            country.     valueProperty().bindBidirectional(client.countryProperty());
            statuses.      valueProperty().bindBidirectional(client.statusProperty());
        }
    }
    
    @Override
    protected void makeExtraActions(EDITOR_BUTTON_TYPE buttonType) {
        openDate.setDisable(true);
        boolean editable = true;
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            editable = false;
        }
        Client client = (Client)sceneObj;
        if (client != null){
            PhoneComboBox phonesCombobox = new PhoneComboBox(client.getPhones(), editable);
            phonesContainer.getChildren().add(phonesCombobox);
            if (!buttonType.equals(EDITOR_BUTTON_TYPE.ADD) && (client.getEmail() == null || client.getEmail().isEmpty())){
                email.setDisable(true);
            }
            if (client.getIsJur()){
                changeSceneVisualAsFirm(" ");
            }
            imageGalleryController.setURLData(serviceURLPrefix, client.getRecId() + "/", client.getRecId() + "/all");
            List<String> imageNames = client.getDocuments().stream().map((Client.Document doc) -> doc.path).collect(Collectors.toList());
            imageGalleryController.downloadData(imageNames);

            client.setClientImageGallery(imageGalleryController);
            statusProperty.bind(client.statusProperty()); // static variable ???????????????
        }
        
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    
    private class BackgroundAccessToDB implements Runnable {

        private final String pathCities;
        
        public BackgroundAccessToDB(String pathForCities){
            pathCities = pathForCities;
        }
        
        @Override
        public void run() {
            try {
                DBClient dbClinet = GeneralConfig.getInstance().getDBClient();
                String responseAsString = dbClinet.get(pathCities).getDataAsString();
                JSONArray cities = new JSONArray(responseAsString);
                List<String> citiesAsList = getListFromJSONArray(cities);
                TextFields.bindAutoCompletion(  city,
                                                (AutoCompletionBinding.ISuggestionRequest param) -> citiesAsList.stream().filter((cityName) ->
                                                    cityName.toLowerCase().contains(param.getUserText().toLowerCase()) )
                                                .collect(Collectors.toList()), 
                                                getStringConverter());
                
            } catch (AuthServerException | IOException | JSONException ex) {
                Logger.getLogger(ClientDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private List<String> getListFromJSONArray(JSONArray json) throws JSONException{
            List<String> result = new ArrayList<>();
            for (int i = 0; i < json.length(); i++){
                String currCity = json.getString(i).trim();
                if (!currCity.isEmpty())
                    result.add(json.getString(i));
            }
            return result;
        }
        
        private StringConverter<String> getStringConverter(){
            return new StringConverter<String>() {
                @Override
                public String toString(String name) {
                    return name;
                }

                @Override
                public String fromString(String string) {
                    return string;
                }
            };
        }
    }
    
    
    public static class CustomPredicate implements Predicate<String> {

        public CustomPredicate(){}
        
        @Override
        public boolean test(String fieldName) {
            return statusProperty.get() != null && statusProperty.get().getClientStatusId() != 1;
        }

        @Override
        public Predicate<String> and(Predicate<? super String> other) {
            return Predicate.super.and(other);
        }

        @Override
        public Predicate<String> negate() {
            return Predicate.super.negate();
        }

        @Override
        public Predicate<String> or(Predicate<? super String> other) {
            return Predicate.super.or(other);
        }
        
    }
}
