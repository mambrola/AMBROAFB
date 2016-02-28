/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.invoices.Invoice;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ProductsController implements Initializable {

    @FXML
    private TableView<Product> table;
    
    @FXML private void tm(ActionEvent e) {System.out.println("pressed: " + "Pictogram");}
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product.dbGetProducts(0).values().stream().forEach((product) -> {
            table.getItems().add(product);
        });
    }    
    
}
