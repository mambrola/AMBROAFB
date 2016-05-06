/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

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
    
    @FXML
    private void delete(ActionEvent e) {
        System.out.println("passed: Delete");
    }

    @FXML
    private void edit(ActionEvent e) {
        Product selectedObject = table.getSelectionModel().getSelectedItem();
//        ProductDialog dialog = new ProductDialog(selectedObject);
//        dialog.showAndWait();
//        if (dialog.isCancelled()) {
//            System.out.println("dialog is cancelled");
//        } else {
//            System.out.println("changed client: " + dialog.getResult());
//        }
    }

    @FXML
    private void view(ActionEvent e) {
        Product selectedObject = table.getSelectionModel().getSelectedItem();
//        ProductDialog dialog = new ProductDialog(selectedObject);
//        dialog.setDisabled();
//        dialog.askClose(false);
//        dialog.showAndWait();
    }

    @FXML
    private void add(ActionEvent e) {
//        ProductDialog dialog = new ProductDialog();
//        dialog.showAndWait();
//
//        if (dialog.isCancelled()){
//            System.out.println("dialog is cancelled addClient");
//        }else{
//            System.out.println("changed client: "+dialog.getResult());
//        }
    }
    
    @FXML 
    private void addBySample(ActionEvent e) {
//        Product selectedObject = table.getSelectionModel().getSelectedItem();
//        ProductDialog dialog = new ProductDialog(selectedObject);
//        dialog.resetClient();
//        dialog.showAndWait();
//        if (dialog.isCancelled()){
//            System.out.println("dialog is cancelled addBySample");
//        }else{
//            System.out.println("changed client: "+dialog.getResult());
//
//        }
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        table.getItems().clear();
        asignTable();
    }
    /**
     * 
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignTable();
    }

    private void asignTable() {
        Product.dbGetProducts(0).values().stream().forEach((product) -> {
            table.getItems().add(product);
        });
        //panel.disablePropertyBinder(table);
    }
    
}
