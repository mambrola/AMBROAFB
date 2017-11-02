/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.helper;

import ambroafb.invoices.InvoiceDataFetchProvider;
import authclient.AuthServerException;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dkobuladze
 */
public class InvoiceStatusClarifyComboBox extends ComboBox<InvoiceStatusClarify> {
    
    private final ObservableList<InvoiceStatusClarify> items = FXCollections.observableArrayList();
    private final InvoiceDataFetchProvider dataFetchProvider = new InvoiceDataFetchProvider();
    
    public InvoiceStatusClarifyComboBox(){
        super();
        setItems(items);
        
    }
    
    
    public void fillComboBox(Consumer<ObservableList<InvoiceStatusClarify>> extraAxtion){
        new Thread(new FetchDataFromDB(extraAxtion)).start();
    }

    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<InvoiceStatusClarify>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<InvoiceStatusClarify>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<InvoiceStatusClarify> clarifies = dataFetchProvider.getAllIvoiceClarifiesFromDB();
                clarifies.sort((InvoiceStatusClarify clarify1, InvoiceStatusClarify clarify2) -> clarify1.compareById(clarify2));
                Platform.runLater(() -> {
                    items.setAll(clarifies);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            } catch (IOException | AuthServerException ex) {
            }
        }
        
    }
    
}
