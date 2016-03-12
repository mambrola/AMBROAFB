/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.viewadd;

import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class AddClientController implements Initializable {

    GeneralConfig conf = GeneralConfig.getInstance();
    ArrayList<Node> focusTraversableNodes;
    HashMap<String, String> textFieldValues = new HashMap<>();
    
    @FXML VBox formPane;
    @FXML private Label first_name, last_name;
    @FXML DatePicker openDate;
    @FXML CheckBox juridical, rezident;
    @FXML TextField firstName, lastName, idNumber, email, fax, address, zipCode, city;
    @FXML ComboBox country;
    
    
    @FXML 
    private void switchJuridical (ActionEvent e) {
        System.out.println("e.getSource(): " + firstName.widthProperty().getValue());
        double w = firstName.widthProperty().getValue() + lastName.widthProperty().getValue();
        if(((CheckBox)e.getSource()).isSelected()){
            first_name.setText(conf.getTitleFor("firm_name"));
            last_name.setText(conf.getTitleFor("firm_form"));
            firstName.setPrefWidth(0.75*w);
            lastName.setPrefWidth(0.25*w);
        } else {
            first_name.setText(conf.getTitleFor("first_name"));
            last_name.setText(conf.getTitleFor("last_name"));
            firstName.setPrefWidth(0.50*w);
            lastName.setPrefWidth(0.50*w);
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
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
                        textFieldValues.put(node.getId(), ((TextField) node).getText());
                    }
                });
            }
            if(node.getClass().equals(Button.class)) {
                ((Button) node).pressedProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue && !newValue && node.isHover()) {
                        node.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.ENTER, false, false, false, false ));
                    }
                });
            }
            node.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    switch (node.getId()) {
                        case("ok"):
                            saveClient();
                            break;
                        case("cancel"):
                            cancel();
                            break;    
                        default:
                            node.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, false, false, false, false ));
                    } 
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE)  {
                    if(node.getClass().equals(TextField.class))
                        ((TextField)node).setText(textFieldValues.get(node.getId()));
                }
            });
        });
    }    
    
    private void saveClient(){
        System.out.println("method 'saveClient'");
        ((Stage)formPane.getScene().getWindow()).close();
    }
    private void cancel(){
        if(new AlertMessage(Alert.AlertType.CONFIRMATION, null, "Do you want to exit without saving?").showAndWait().get().equals(ButtonType.OK))        
            ((Stage)formPane.getScene().getWindow()).close();
    }
}
