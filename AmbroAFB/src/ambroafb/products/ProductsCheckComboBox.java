/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dato
 */
public class ProductsCheckComboBox extends CheckComboBox<Product> {
    
    public ProductsCheckComboBox(){
        this.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product.getDescrip() + "\t" + product.getRemark();
            }

            @Override
            public Product fromString(String string) {
                return null;
            }
        });
        this.getItems().setAll(Product.dbGetProducts());
    }
    
    
}
