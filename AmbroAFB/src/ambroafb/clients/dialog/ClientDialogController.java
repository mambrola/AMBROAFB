/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.general.Editable;
import ambroafb.general.ListEditor;
import ambroafb.general.okay_cancel.OkayCancel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    private ListEditor<? extends Editable<String>> phone;
    @FXML
    private TextField fax;
    @FXML
    private TextField address;
    @FXML
    private TextField zipCode;
    @FXML
    private TextField city;
    @FXML
    private ComboBox<?> country;
    @FXML
    private CheckBox rezident;
    @FXML
    private OkayCancel okayCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
