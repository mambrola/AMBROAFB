/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.helper;

import ambroafb.licenses.LicenseDataFetchProvider;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dkobuladze
 */
public class LicenseStatusCheckComboBox extends CheckComboBox<LicenseStatus> {
    
    private final LicenseDataFetchProvider dataFetchProvider = new LicenseDataFetchProvider();
    
    public LicenseStatusCheckComboBox(){
        super();
    }
    
    public void fillComboBox(Consumer<ObservableList<LicenseStatus>> extraAxtion){
        new Thread(new FetchDataFromDB(extraAxtion)).start();
    }

    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<LicenseStatus>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<LicenseStatus>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<LicenseStatus> statuses = dataFetchProvider.getAllLicenseStatusFromDB();
                statuses.sort((LicenseStatus status1, LicenseStatus status2) -> status1.getRecId() - status2.getRecId());
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
