/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.interfaces.DataFetchProvider;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dato
 */
public class ProductsCheckComboBox extends CheckComboBox<Product> {
    
    private final ProductDataFetchProvider dataFetchProvider = new ProductDataFetchProvider();
    
    public ProductsCheckComboBox(){
        this.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product.getAbbreviation() + " " + product.getFormer();
            }

            @Override
            public Product fromString(String string) {
                return null;
            }
        });
        try {
            this.getItems().setAll(dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL));
        } catch (Exception ex) {
        }
    }
    
    
}
