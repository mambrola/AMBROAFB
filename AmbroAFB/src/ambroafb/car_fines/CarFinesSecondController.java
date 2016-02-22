/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.car_fines;

import ambro.ATableView;
import ambroafb.general.GeneralConfig;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class CarFinesSecondController implements Initializable {

    @FXML
    private ATableView<CarAllData> table;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setBundle(rb);
        table.getItems().addAll((Collection<CarAllData>)GeneralConfig.getInstance().getAttribute("car_data"));
    }    

 
    
}
