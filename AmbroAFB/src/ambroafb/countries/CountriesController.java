/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
       
            Country.getAllFromDB().stream().forEach((country) -> {
                table.getItems().add(country);
            });
    }

}
