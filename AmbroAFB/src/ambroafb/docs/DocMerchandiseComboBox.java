/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import java.util.List;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dkobuladze
 */
public class DocMerchandiseComboBox extends ComboBox<DocMerchandise> {
    
    private final DocDataFetchProvider dataFetchProvider = new DocDataFetchProvider();
    
    public DocMerchandiseComboBox(){
        super();

        new Thread(new FetchDataRunnable(this.getItems())).start();
    }
    
    private class FetchDataRunnable implements Runnable {

        private ObservableList<DocMerchandise> items;
        
        public FetchDataRunnable(ObservableList<DocMerchandise> items){
            this.items = items;
        }
        
        @Override
        public void run() {
            try {
                List<DocMerchandise> merchandises = dataFetchProvider.getDocMerchandises();
                merchandises.sort((DocMerchandise m1, DocMerchandise m2) -> m1.compareById(m2));
                Platform.runLater(() -> {
                    items.setAll(merchandises);
                });
            } catch (Exception ex) {
            }
        }
        
    }
}
