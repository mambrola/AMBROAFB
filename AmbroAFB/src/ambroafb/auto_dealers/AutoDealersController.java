/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.auto_dealers;

import ambro.ATableView;
import ambroafb.general.GeneralConfig;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class AutoDealersController implements Initializable {

    private GeneralConfig config;
    
    @FXML
    private ComboBox operation;
    
    @FXML
    private ATableView<Products> tableProducts;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = GeneralConfig.getInstance();
        
        ObservableList<String> operations = FXCollections.observableArrayList(
            config.getTitleFor("order_on_products"),
            config.getTitleFor("prescribe_invoices"),
            config.getTitleFor("interrupt_invoices"),
            config.getTitleFor("replace_invoices"),
            config.getTitleFor("enrollment_money"),
            config.getTitleFor("accrual")
        );
        operation.setItems(operations);
        operation.setPromptText(config.getTitleFor("choose_operation"));
        
        tableProducts.getItems().add(new Products("inv", "product", true));
        tableProducts.getItems().add(new Products("inv", "product", true));
        tableProducts.getItems().add(new Products("inv", "product", false));
        tableProducts.getItems().add(new Products("inv", "product", true));
        tableProducts.getItems().add(new Products("inv", "product", true));
        tableProducts.getItems().add(new Products("inv", "product", true));
        
    }   
    @FXML
    public void print (){
        System.out.println("" + tableProducts.getItems().get(0).isMarked);
    }
    
}
