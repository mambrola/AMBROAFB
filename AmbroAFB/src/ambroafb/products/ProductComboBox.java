/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class ProductComboBox extends ComboBox<Product> {
    
    public ProductComboBox(){
        setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product.getAbbreviation() + ",  " + product.getFormer();
            }

            @Override
            public Product fromString(String data) {
                return null;
            }
        });

        this.getItems().addAll(Product.getAllFromDB());
    }
    
    public void selectItem(Product product){
        this.getSelectionModel().select(product);
    }
}
