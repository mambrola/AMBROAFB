/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.viewadd.client_dialog;

import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class ClientDialogController implements Initializable {

    GeneralConfig conf = GeneralConfig.getInstance();
    ArrayList<Node> focusTraversableNodes;
//    HashMap<String, String> textFieldValues = new HashMap<>();

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
    ComboBox country;

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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Country.dbGetCountries("").values().stream().forEach((c) -> {
            country.getItems().add(c.getFullDescrip());
        });
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        int countNodes = focusTraversableNodes.size();
        focusTraversableNodes.stream().forEach((node) -> {
            if (node.getClass().equals(TextField.class)) {
                ((TextField) node).focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!oldValue && newValue) {
                        node.setUserData(((TextField) node).getText());
//                        textFieldValues.put(node.getId(), ((TextField) node).getText());
                    }
                });
            }
            if (node.getClass().equals(Button.class)) {
                ((Button) node).pressedProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue && !newValue && node.isHover()) {
                        node.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.ENTER, false, false, false, false));
                    }
                });
            }
            node.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    switch (node.getId()) {
                        case ("ok"):
                            saveClient();
                            break;
                        case ("cancel"):
                            cancel();
                            break;
                        default:
                            node.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, false, false, false, false));
                    }
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    if (node.getClass().equals(TextField.class)) {
                        ((TextField) node).setText((String) node.getUserData());
                    }
                }
            });
        });
    }

    private void saveClient() {
        System.out.println("method 'saveClient'");
        if (onCreate != null) {
            Client c = new Client(new Object[]{null, juridical.isSelected(), rezident.isSelected(), firstName.getText(), lastName.getText(), email.getText(), address.getText(), zipCode.getText(), city.getText(), null, country.getValue(), idNumber.getText(), null, fax.getText()});
            onCreate.accept(c); // null-ის მაგივრად გადავცემთ შექმნილ კლიენტს
        }
    }

    private void cancel() {
        if (new AlertMessage(Alert.AlertType.CONFIRMATION, null, "Do you want to exit without saving?").showAndWait().get().equals(ButtonType.OK)) {
            if (onCancell != null) {
                onCancell.accept(null);
            }
        }
    }

    public void onCreate(Consumer<Client> callback) {
        onCreate = callback;
    }

    public void onCancell(Consumer<Void> callback) {
        onCancell = callback;
    }
    
    public void setDisabled(){
        
    }
    
    public void setClient(Client client){
        firstName.setText(client.firstName);
        lastName.setText(client.lastName);
    }

}
