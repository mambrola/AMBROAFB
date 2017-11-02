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
public class InvoiceReissuingCheckComboBox extends CheckComboBox<InvoiceReissuing> {
    
    private final InvoiceDataFetchProvider dataFetchProvider = new InvoiceDataFetchProvider();
    
    public InvoiceReissuingCheckComboBox(){
        super();
        
    }
    
    public void fillComboBox(Consumer<ObservableList<InvoiceReissuing>> extraAxtion){
        new Thread(new FetchDataFromDB(extraAxtion)).start();
    }

    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<InvoiceReissuing>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<InvoiceReissuing>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<InvoiceReissuing> reissuings = dataFetchProvider.getAllIvoiceReissuingsesFromDB();
                reissuings.sort((InvoiceReissuing reissuing1, InvoiceReissuing reissuing2) -> reissuing1.getRecId() - reissuing2.getRecId());
                Platform.runLater(() -> {
                    getItems().setAll(reissuings);
                    if (consumer != null){
                        consumer.accept(getItems());
                    }
                });
            } catch (IOException | AuthServerException ex) {
            }
        }
        
    }
}
