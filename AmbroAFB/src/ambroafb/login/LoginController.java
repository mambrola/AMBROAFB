/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.login;

import ambroafb.AmbroAFB;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import authclient.AuthServerException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class LoginController extends Stage implements Initializable {

    private static final String PREFS_USERNAME = "kfz_saved_username";

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label status;
    @FXML
    private MaskerPane masker;
    @FXML
    private Button login;

    private boolean loggedIn;
    private final Preferences prefs;

    public LoginController() {
        super();

        prefs = Preferences.userNodeForPackage(AmbroAFB.class);
        Scene scene = Utils.createScene("/ambroafb/login/Login.fxml", (Object)this);
        this.setScene(scene);
        setResizable(false);
        login.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER))
                login();
        });
        if (!username.getText().isEmpty()){
            password.requestFocus();
        }
    }

    public boolean prompt() {
        showAndWait();
        return loggedIn;
    }

    public void login() {
        masker.setVisible(true);
        new Thread(() -> {
            try {
                GeneralConfig.getInstance().getAuthClient(username.getText(), password.getText()).login();
                loggedIn = true;
                prefs.put(PREFS_USERNAME, username.getText());
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    close();
                });
                return;
            } 
            catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    new AlertMessage(Alert.AlertType.ERROR, ex, "Network Error", getClass().getSimpleName()).showAlert();
                });
            } 
            catch (AuthServerException ex) {
                if (ex.getStatusCode() == 401) {
                    Platform.runLater(() -> {
                        masker.setVisible(false);
                        status.setText(GeneralConfig.getInstance().getTitleFor("invalide_username_password"));
                    });
                }
            }
            loggedIn = false;
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(prefs.get(PREFS_USERNAME, ""));
    }

}
