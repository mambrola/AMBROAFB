/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.interfaces.DataFetchProvider;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class DocCodeComboBox extends ComboBox<DocCode> {
    
    public static final String categoryALL = "ALL";
    private final DocCode docCodeALL = new DocCode();
    private final  ObservableList<DocCode> items = FXCollections.observableArrayList();
    private final DocDataFetchProvider dataFetchProvider = new DocDataFetchProvider();
    
    public DocCodeComboBox(){
        super();
        setItems(items);
        this.setEditable(true);
        this.setConverter(new customStringConverter());
        
        docCodeALL.setDocCode(categoryALL);
    }
    
    /**
     * The method fills comboBox by docCodes, ALL category item and then calls consumer.
     * @param extraAction The extra action on comboBox filling. If consumer is null, no extra action execute.
     */
    public void fillComboBoxWithALL(Consumer<ObservableList<DocCode>> extraAction){
        Consumer<ObservableList<DocCode>> addCategoryALL = (docCodes) -> {
            docCodes.add(0, docCodeALL);
            setValue(docCodeALL);
        };
        Consumer<ObservableList<DocCode>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        new Thread(new FetchDataFromDB(consumer)).start();
    }
    
    public void fillComboBoxWithoutALL(Consumer<ObservableList<DocCode>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
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

        private final Consumer<ObservableList<DocCode>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<DocCode>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                List<DocCode> itemsFromDB = dataFetchProvider.getDocCodes(DataFetchProvider.PARAM_FOR_ALL);
                Platform.runLater(() -> {
                    items.setAll(itemsFromDB);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            } catch (Exception ex) {
            }
        }
    }
    
}
