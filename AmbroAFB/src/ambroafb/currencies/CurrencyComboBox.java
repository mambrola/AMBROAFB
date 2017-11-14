/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import authclient.db.ConditionBuilder;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class CurrencyComboBox extends ComboBox<Currency>{
    
    public static final String categoryALL = "ALL";
    private final ObservableList<Currency> items = FXCollections.observableArrayList();
    private final FilteredList<Currency> filteredList = new FilteredList<>(items);
    private final Currency currencyALL = new Currency();
    private final CurrencyDataFetchProvider dataFetchProvider = new CurrencyDataFetchProvider();
    
    public CurrencyComboBox(){
        super();
        this.setItems(filteredList);
        currencyALL.setIso(categoryALL);
    }
    
    public void fillComboBoxWithALL(Consumer<ObservableList<Currency>> extraAction){
        Consumer<ObservableList<Currency>> addALLCategory = (currencies) -> {
            if (!currencies.contains(currencyALL)){
                currencies.add(0, currencyALL);
                setValue(currencyALL);
            }
        };
        Consumer<ObservableList<Currency>> consumer = (extraAction == null) ? addALLCategory : addALLCategory.andThen(extraAction);
        new Thread(new FetchDataFromDB(consumer)).start();
    }
    
    
    public void fillComboBoxWithoutALL(Consumer<ObservableList<Currency>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    
    public void fillComboBoxWithALLAndWithoutRatesBasicIso(Consumer<ObservableList<Currency>> extraAction){
        Consumer<ObservableList<Currency>> removeRatesBasicIso = (currencies) -> {
            new Thread(new FetchRatesBasicIso()).start();
        };
        Consumer<ObservableList<Currency>> consumer = (extraAction == null) ? removeRatesBasicIso : removeRatesBasicIso.andThen(extraAction);
        fillComboBoxWithALL(consumer);
    }
    
    
    public void fillComboBoxWithoutALLAndWithoutRatesBasicIso(Consumer<ObservableList<Currency>> extraAction){
        Consumer<ObservableList<Currency>> removeRatesBasicIso = (currencies) -> {
            new Thread(new FetchRatesBasicIso()).start();
        };
        Consumer<ObservableList<Currency>> consumer = (extraAction == null) ? removeRatesBasicIso : removeRatesBasicIso.andThen(extraAction);
        fillComboBoxWithoutALL(consumer);
    }
    
//    private void setShowCategoryALL(boolean showCategoryALL) {
//        if (!showCategoryALL){
//            if (getItems().contains(currencyALL))
//                items.remove(currencyALL);
//        }
//        else {
//            if (!getItems().contains(currencyALL))
//                items.add(0, currencyALL);
//        }
//    }
    
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

        public FetchRatesBasicIso() {
        }

        @Override
        public void run() {
            try {
                JSONArray jsonArr = GeneralConfig.getInstance().getDBClient().select("basic_params", new ConditionBuilder().where().and("param", "=", "rates_basic_iso").condition().build());
                String removedIso = (String)((JSONObject)jsonArr.opt(0)).opt("value");
                removeCurrency(removedIso);
            } catch (Exception ex) {
                Logger.getLogger(CurrencyComboBox.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Currency>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<Currency>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                List<Currency> currencies = dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL);
                Platform.runLater(() -> {
                    items.setAll(currencies);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            } catch (Exception ex) {
            }
        }
    }
}