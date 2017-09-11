/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.DBUtils;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
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
        new Thread(new FetchDataFromDB(this.getItems())).start();
    }
    
    private class customStringConverter extends StringConverter<DocCode> {

        @Override
        public String toString(DocCode object) {
            return object.getDocCode();
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
        private final ObservableList<DocCode> items;
        
        public FetchDataFromDB(ObservableList<DocCode> items) {
            this.items = items;
        }

        @Override
        public void run() {
            JSONObject params = new ConditionBuilder().build();
            ArrayList<DocCode> itemsFromDB = DBUtils.getObjectsListFromDB(DocCode.class, DB_VIEW_NAME, params);
            Platform.runLater(() -> {
                items.setAll(itemsFromDB);
            });
        }
    }
    
}
