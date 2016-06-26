/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.configuration;

import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class ConfigurationController implements Initializable {

    @FXML
    private ComboBox<String> language;

    @FXML
    private TextField database;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button restart;

    @FXML
    private void save() {
        try {
            GeneralConfig.getInstance().change(language.getValue(), database.getText(), username.getText(), password.getText());
        } catch (SQLException ex) {
            AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR);
            alert.showAlert();
        }
    }

    @FXML
    private void restart() {
        save();
        Utils.restart();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GeneralConfig conf = GeneralConfig.getInstance();
        language.setValue(conf.getLanguage());
        database.setText(conf.getDatabase());
        username.setText(conf.getUsername());
        password.setText(conf.getPassword());

        language.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean changed = !newValue.equals(GeneralConfig.getInstance().getLanguage());
            restart.setVisible(changed);
            if (changed) {
                restart.setText(conf.getTitleForLanguage("needs_restart", newValue));
            }
        });
    }
}
