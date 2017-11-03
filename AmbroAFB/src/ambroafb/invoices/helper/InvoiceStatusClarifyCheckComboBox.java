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
import javafx.collections.ObservableList;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dkobuladze
 */
public class InvoiceStatusClarifyCheckComboBox extends CheckComboBox<InvoiceStatusClarify> {
    
    private final InvoiceDataFetchProvider dataFetchProvider = new InvoiceDataFetchProvider();
    
    public InvoiceStatusClarifyCheckComboBox(){
        super();
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
                    getItems().setAll(clarifies);
                    if (consumer != null){
                        consumer.accept(getItems());
                    }
                });
            } catch (Exception ex) {
            }
        }
        
    }
}
