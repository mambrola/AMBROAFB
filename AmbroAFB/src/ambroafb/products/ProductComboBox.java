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
    
    public static final Product productALL = new Product();
    
    private static final String ALL = "ALL";
    
    public ProductComboBox(){
        setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                String result = null;
                if (product != null){
                    result = product.getDescrip();
                }
                return result;
            }

            @Override
            public Product fromString(String data) {
                return null;
            }
        });

        productALL.setDescrip(ALL);
        productALL.setRecId(0);
        this.getItems().add(productALL);
        this.getItems().addAll(Product.getAllFromDB());
        this.setValue(productALL);
    }
    
    public void selectItem(Product product){
        this.getSelectionModel().select(product);
    }
}
