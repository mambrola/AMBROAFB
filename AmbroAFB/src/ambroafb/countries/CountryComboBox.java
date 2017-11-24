/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.countries;

import ambroafb.general.interfaces.DataProvider;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class CountryComboBox extends ComboBox<Country> {
    
    public static final String categoryALL = "ALL";
    private final ObservableList<Country> items = FXCollections.observableArrayList();
    private final Country countryALL = new Country();
    private final CountryDataFetchProvider dataFetchProvider = new CountryDataFetchProvider();
    
    public CountryComboBox(){
        super();
        this.setItems(items);
        countryALL.setRecId(0);
        countryALL.setCode(categoryALL);
        countryALL.setDescrip("");

        this.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.toString();
            }
            @Override
            public Country fromString(String string) {
                return null;
            }
        });
    }
    
    
    /**
     *  The method fills comboBox by countries and category ALL.
     * @param extraAction The action that executes after comboBox filling. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxWithALL(Consumer<ObservableList<Country>> extraAction){
        Consumer<ObservableList<Country>> addCategoryALL = (countriesList) -> {
            countriesList.add(0, countryALL);
            setValue(countryALL);
        };
        Consumer<ObservableList<Country>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        fillComboBoxWithouyALL(consumer);
    }
    
    /**
     *  The method fills comboBox by countries.
     * @param extraAction The action that executes after comboBox filling. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxWithouyALL(Consumer<ObservableList<Country>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    public void selectItem(Country country){
        this.getSelectionModel().select(country);
    }

    public void showCategoryAll(boolean show){
        if (!show){
            if (getItems().contains(countryALL)){
                getItems().remove(0);
            }
        }
        else {
            if (!getItems().contains(countryALL)){
                getItems().add(countryALL);
            }
        }
    }
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Country>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<Country>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<Country> countries = dataFetchProvider.getOrderedByClients();
                Platform.runLater(() -> {
                    items.setAll(countries);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            } catch (Exception ex) {
            }
        }
        
    }
}