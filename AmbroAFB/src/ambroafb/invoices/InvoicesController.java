/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

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
public class InvoicesController implements Initializable {

    @FXML private TableView<Invoice> table;
    
    @FXML
    private void delete(ActionEvent e) {
        Invoice invoice = table.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    private void edit(ActionEvent e) {
        Invoice invoice = table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void view(ActionEvent e) {
        Invoice invoice = table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void add(ActionEvent e) {
    }

    @FXML
    private void addBySample(ActionEvent e) {
        Invoice invoice = table.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void refresh(ActionEvent e) {
        table.getItems().clear();
        assignTable();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assignTable();
    }

    private void assignTable() {
        Invoice.dbGetInvoices(0).values().stream().forEach((invoice) -> {
            table.getItems().add(invoice);
        });
        //panel.disablePropertyBinder(table);
    }
}
