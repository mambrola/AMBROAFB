/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.AlertMessage;
import ambroafb.general.KFZClient;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author murman
 */
public class CountriesController implements Initializable {

    @FXML
    private TableView<Country> table;

    @FXML
    private void tm(ActionEvent e) {
        System.out.println("pressed: " + "Pictogram");
        refreshCountries();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshCountries();
    }

    private void refreshCountries() {
        try {
            Country.getCountries().stream().forEach((country) -> {
                table.getItems().add(country);
            });
        } catch (KFZClient.KFZServerException | IOException ex) {
            Logger.getLogger(CountriesController.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
    }

}
