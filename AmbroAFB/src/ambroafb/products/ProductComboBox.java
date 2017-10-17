/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class ProductComboBox extends ComboBox<Product> {
    
    private static final String categoryAll = "ALL";
    private final Product productALL = new Product();
    
    private final ObservableList<Product> items = FXCollections.observableArrayList();
    
    public ProductComboBox(){
        setItems(items);

        productALL.setDescrip(categoryAll);
    }
    
    /**
     * The method fills comboBox by products data and category ALL.
     * @param extraAction The extra action on comboBox filling. If there is no extra action exists, gives null value.
     */
    public void fillComboBoxWithALL(Consumer<ObservableList<Product>> extraAction){
        Consumer<ObservableList<Product>> addCategoryALL = (producList) -> {
            producList.add(0, productALL);
            setValue(productALL);
        };
        Consumer<ObservableList<Product>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        fillComboBoxWithoutALL(consumer);
    }
    
    /**
     * The method fills comboBox  by products data, without category ALL.
     * @param extraAction The extra action on comboBox filling. If there is no extra action exists, gives null value.
     */
    public void fillComboBoxWithoutALL(Consumer<ObservableList<Product>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Product>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<Product>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            ArrayList<Product> products = Product.getAllFromDB();
            Platform.runLater(() -> {
                items.setAll(products);
                if (consumer != null){
                    consumer.accept(items);
                }
            });
        }
        
    }
}
