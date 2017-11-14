/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.products.helpers.ProductSpecific;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class ProductsSpecificsComboBox extends ComboBox<ProductSpecific> {
    
    private final ObservableList<ProductSpecific> items = FXCollections.observableArrayList();
    private final ProductDataFetchProvider dataFetchProvider = new ProductDataFetchProvider();
    
    public ProductsSpecificsComboBox(){
        this.setItems(items);
        
        try {
            dataFetchProvider.getAllSpecificsFromDB().stream().forEach((specific) -> {
                items.add(specific);
            });
        } catch (Exception ex) {
        }
    }
    
}
