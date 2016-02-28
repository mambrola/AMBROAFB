/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambroafb.invoices.Invoice;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
public class InvoicesController implements Initializable {

    @FXML
    private TableView<Invoice> table;
    
    @FXML private void tm(ActionEvent e) {System.out.println("pressed: " + "Pictogram");}
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        table.getItems().addAll(Invoice.dbGetInvoice(0));
    Invoice.dbGetInvoices(0).values().stream().forEach((invoice) -> {
            table.getItems().add(invoice);
        });
    }    
}
