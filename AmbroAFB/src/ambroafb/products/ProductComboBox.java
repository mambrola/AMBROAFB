/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import java.util.ArrayList;
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
        ArrayList<Product> productList = Product.getAllFromDB();
        productList.add(productALL);
        productList.sort((Product p1, Product p2) -> p1.getRecId() - p2.getRecId());
        
        this.getItems().addAll(productList);
        this.setValue(productALL);
    }
    
    public void selectItem(Product product){
        this.getSelectionModel().select(product);
    }
}
