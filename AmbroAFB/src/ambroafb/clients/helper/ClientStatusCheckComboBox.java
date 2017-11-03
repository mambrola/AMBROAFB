/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.helper;

import ambroafb.clients.ClientDataFetchProvider;
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
public class ClientStatusCheckComboBox extends CheckComboBox<ClientStatus> {
    
    private final ClientDataFetchProvider dataFetchProvider = new ClientDataFetchProvider();
    
    public ClientStatusCheckComboBox(){
        super();
        
    }
    
    public void fillComboBox(Consumer<ObservableList<ClientStatus>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    private class FetchDataFromDB implements Runnable {
        
        private final Consumer<ObservableList<ClientStatus>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<ClientStatus>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<ClientStatus> statuses = dataFetchProvider.getClientStatuses();
                statuses.sort((ClientStatus status1, ClientStatus status2) -> status1.compateById(status2));
                Platform.runLater(() -> {
                    getItems().setAll(statuses);
                    if (consumer != null){
                        consumer.accept(getItems());
                    }
                });
            } catch (Exception ex) {
            }
        }
        
    }
}
