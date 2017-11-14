/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class IsoComboBox extends ComboBox<String> {
    
    private final ObservableList<String> items = FXCollections.observableArrayList();
    private final FilteredList<String> filteredList = new FilteredList<>(items);
    private final String GEL_ISO = "GEL";
    private final CurrencyDataFetchProvider dataFetchProvider = new CurrencyDataFetchProvider();
    
    public IsoComboBox(){
        super();
        this.setItems(filteredList);
        this.setConverter(new CustomIsoConverter());
    }
    
    public void fillComboBox(Consumer<ObservableList<String>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    public void filterBy(String iso){
        if (iso != null){
            filteredList.setPredicate((String boxIso) -> !boxIso.equals(iso));
            if (getValue() != null && getValue().equals(iso)){
                setValue(null);
            }
        }
    }
    
    public void setValueToGEL(){
        FilteredList<String> list = this.getItems().filtered((elem) -> elem.equals(GEL_ISO));
        if (!list.isEmpty()){
            setValue(list.get(0));
        }
    }

    private class CustomIsoConverter extends StringConverter<String> {

        @Override
        public String toString(String selected) {
            return selected;
        }

        @Override
        public String fromString(String input) {
            return input.toUpperCase();
        }
        
    }
    
    private class FetchDataFromDB implements Runnable {

        private Consumer<ObservableList<String>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<String>> consumer) {
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<String> currencies = dataFetchProvider.getAllIsoFromDB();
                Platform.runLater(() -> {
                    items.setAll(currencies);
                    if (consumer != null) consumer.accept(items);
                });
            } catch (Exception ex) {
            }
        }
        
    }
}
