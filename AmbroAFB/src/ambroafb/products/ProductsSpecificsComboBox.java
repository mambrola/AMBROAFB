/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.products.helpers.ProductSpecific;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
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
        
//        try {
//            dataFetchProvider.getAllSpecificsFromDB().stream().forEach((specific) -> {
//                items.add(specific);
//            });
//        } catch (Exception ex) {
//        }
    }
    
    public void fillComboBox(Consumer<ObservableList<ProductSpecific>> extraAxtion){
        new Thread(new FetchDataFromDB(extraAxtion)).start();
    }
    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<ProductSpecific>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<ProductSpecific>> consumer) {
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<ProductSpecific> specifics = dataFetchProvider.getAllSpecificsFromDB();
                Platform.runLater(() -> {
                    items.setAll(specifics);
                    if (consumer != null) consumer.accept(items);
                });
            } catch (Exception ex) {
            }
        }
    }
}
