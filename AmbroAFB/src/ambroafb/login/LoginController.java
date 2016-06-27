/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.login;

import ambroafb.AmbroAFB;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.Utils;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    private boolean loggedIn;
    private final Preferences prefs;

    public LoginController() {
        super();

        prefs = Preferences.systemNodeForPackage(AmbroAFB.class);
        Scene scene = Utils.createScene("/ambroafb/login/Login.fxml", this);
        this.setScene(scene);
    }

    public boolean prompt() {
        showAndWait();
        return loggedIn;
    }

    public void login() {
        masker.setVisible(true);
        new Thread(() -> {
            try {
                GeneralConfig.getInstance().getServerClient(username.getText(), password.getText());
                loggedIn = true;
                prefs.put(PREFS_USERNAME, username.getText());
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    close();
                });
                return;
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    new AlertMessage(Alert.AlertType.ERROR, ex, "Network Error").showAlert();
                });
            } catch (KFZClient.KFZServerException ex) {
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
