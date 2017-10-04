/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.DBUtils;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DocCodeComboBox extends ComboBox<DocCode> {
    
    public DocCodeComboBox(){
        super();
        
        this.setEditable(true);
        this.setConverter(new customStringConverter());
    }
    
    /**
     * The method fills comboBox by docCodes and then calls consumer.
     * @param consumer The extra action on comboBox filling. If there is no extra action exists, gives null value. 
     */
    public void fillComboBox(Consumer<List<DocCode>> consumer){
        new Thread(new FetchDataFromDB(consumer, this.getItems())).start();
    }
    
    private class customStringConverter extends StringConverter<DocCode> {

        @Override
        public String toString(DocCode object) {
            String value = "";
            if (object != null){
                value = object.getDocCode();
            }
            return value;
        }

        @Override
        public DocCode fromString(String string) {
            DocCode docCode = new DocCode();
            docCode.setDocCode(string);
            return docCode;
        }
        
    }

    private class FetchDataFromDB implements Runnable {

        private final String DB_VIEW_NAME = "doc_codes";
        private final Consumer<List<DocCode>> consumer;
        private final ObservableList<DocCode> items;
        
        public FetchDataFromDB(Consumer<List<DocCode>> consumer, ObservableList<DocCode> items) {
            this.consumer = consumer;
            this.items = items;
        }

        @Override
        public void run() {
            JSONObject params = new ConditionBuilder().build();
            ArrayList<DocCode> itemsFromDB = DBUtils.getObjectsListFromDB(DocCode.class, DB_VIEW_NAME, params);
            Platform.runLater(() -> {
                items.setAll(itemsFromDB);
                if (consumer != null){
                    consumer.accept(itemsFromDB);
                }
            });
        }
    }
    
}
