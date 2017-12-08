/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.interfaces.DataProvider;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class CurrencyComboBox extends ComboBox<Currency>{
    
    public static final String categoryALL = "ALL";
    private final ObservableList<Currency> items = FXCollections.observableArrayList();
    private final FilteredList<Currency> filteredList = new FilteredList<>(items);
    private final Currency currencyALL = new Currency();
    private final Consumer<ObservableList<Currency>> addALLCategory;
    private final CurrencyDataFetchProvider dataFetchProvider;
    
    public CurrencyComboBox(){
        super();
        this.setItems(filteredList);
        currencyALL.setIso(categoryALL);
        
        addALLCategory = (currencies) -> {
            if (!currencies.contains(currencyALL)){
                currencies.add(0, currencyALL);
                setValue(currencyALL);
            }
        };
        dataFetchProvider = getDataFetchProvider();
    }
    
    protected CurrencyDataFetchProvider getDataFetchProvider(){
        return new CurrencyDataFetchProvider();
    }
    
    public void fillComboBoxWithoutALLAndWithoutRatesBasicIso(Consumer<ObservableList<Currency>> extraAction){
        new Thread(new FetchDataFromDB(false, extraAction)).start();
    }
    
    public void fillComboBoxWithoutALLAndWithBasicIso(Consumer<ObservableList<Currency>> extraAction){
        new Thread(new FetchDataFromDB(true, extraAction)).start();
    }
    
    public void fillComboBoxWithALLAndWithoutRatesBasicIso(Consumer<ObservableList<Currency>> extraAction){
        Consumer<ObservableList<Currency>> consumer = (extraAction == null) ? addALLCategory : addALLCategory.andThen(extraAction);
        fillComboBoxWithoutALLAndWithoutRatesBasicIso(consumer);
    }
    
    public void fillComboBoxWithALL(Consumer<ObservableList<Currency>> extraAction){
        Consumer<ObservableList<Currency>> consumer = (extraAction == null) ? addALLCategory : addALLCategory.andThen(extraAction);
        fillComboBoxWithoutALLAndWithBasicIso(consumer);
    }
    
    
    /**
     * The method removes appropriate currency on given iso from items.
     * @param iso The iso that currency must be removed.
     */
    public void removeCurrency(String iso){
        Currency target = null;
        for (Currency item : items) {
            if (item.getIso().equals(iso)){
                target = item;
                break;
            }
        }
        if (target != null){
            items.remove(target);
        }
    }

    /**
     * The method sets appropriate value of given iso.
     * @param iso The currency code.
     */
    public void changeValue(String iso) {
        for (Currency item : items) {
            if(item.getIso().equals(iso)){
                setValue(item);
                break;
            }
        }
    }
    
        /**
     * The method uses to set items in comboBox.
     * @param currencies
     */
    public void setAccounts(List<Currency> currencies){
        this.items.setAll(currencies);
    }
    
    /**
     * The method uses to get items from comboBox.
     * @return 
     */
    public ObservableList<Currency> getAccounts(){
        return items;
    }

    private class FetchRatesBasicIso implements Runnable {

        private Consumer<String> consumer;
        
        public FetchRatesBasicIso(Consumer<String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                String basicIso = dataFetchProvider.getBasicIso();
                Platform.runLater(() -> {
                    if (consumer != null) consumer.accept(basicIso);
                });
            } catch (Exception ex) {
                Logger.getLogger(CurrencyComboBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Currency>> consumer;
        private final boolean containsBasicIso;
        
        public FetchDataFromDB(boolean containsBasicIso, Consumer<ObservableList<Currency>> consumer) {
            this.consumer = consumer;
            this.containsBasicIso = containsBasicIso;
        }
        
        @Override
        public void run() {
            try {
                List<Currency> currencies = (containsBasicIso) ? dataFetchProvider.getFilteredBy(DataProvider.PARAM_FOR_ALL) : dataFetchProvider.getCurrenciesWithoutBasic();
                Platform.runLater(() -> {
                    items.setAll(currencies);
                    if (consumer != null) consumer.accept(items);
                });
            } catch (Exception ex) {
            }
        }
    }
}