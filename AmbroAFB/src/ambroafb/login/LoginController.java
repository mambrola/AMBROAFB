/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.login;

import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class LoginController extends Stage implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label status;

    private boolean loggedIn;

    public LoginController() {
        super();

        Scene scene = Utils.createScene("/ambroafb/login/Login.fxml", this);
        this.setScene(scene);
    }

    public boolean prompt() {
        showAndWait();
        return loggedIn;
    }

    public void login() {
        try {
            GeneralConfig.getInstance().getServerClient(username.getText(), password.getText());
            loggedIn = true;
            close();
            return;
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KFZClient.KFZServerException ex) {
            if (ex.getStatusCode() == 401) {
                status.setText(GeneralConfig.getInstance().getTitleFor("invalide_username_password"));
            }
        }
        loggedIn = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
