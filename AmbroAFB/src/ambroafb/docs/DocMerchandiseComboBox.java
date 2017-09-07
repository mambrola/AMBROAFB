/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.DBUtils;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dkobuladze
 */
public class DocMerchandiseComboBox extends ComboBox<DocMerchandise> {
    
    public DocMerchandiseComboBox(){
        super();

        new Thread(new FetchDataRunnable(this.getItems())).start();
    }
    
    private class FetchDataRunnable implements Runnable {

        private final String procedureName = "utility_get_merchandises";
        private ObservableList<DocMerchandise> items;
        
        public FetchDataRunnable(ObservableList<DocMerchandise> items){
            this.items = items;
        }
        
        @Override
        public void run() {
            ArrayList<DocMerchandise> merchandises = DBUtils.getObjectsListFromDBProcedure(DocMerchandise.class, procedureName);
            merchandises.sort((DocMerchandise m1, DocMerchandise m2) -> m1.compares(m2));
            Platform.runLater(() -> {
                items.setAll(merchandises);
            });
        }
        
    }
}
