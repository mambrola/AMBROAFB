/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class CurrencyRatesComboBox extends ComboBox<String>{
    
    private String showOnlyCurrencies;
    
    private final ObservableList<String> items = FXCollections.observableArrayList();
    
    public CurrencyRatesComboBox(){
        this.setItems(items);
        this.setDisable(true);
        giveCurrencyRatesFromDB();
    }
    
    private void giveCurrencyRatesFromDB(){
        new Thread(() -> {
            items.add(CurrencyRate.ALL_CURRENCY);
            CurrencyRate.getAllCurrencyFromDBTest().stream().forEach((rate) -> {
                items.add(rate);
            });
            Platform.runLater(() -> {
                this.setDisable(false);
            });
        }).start();
    }

    
    public void setShowOnlyCurrencies(String showOnlyCurrencies){
        this.showOnlyCurrencies = showOnlyCurrencies;
        if (showOnlyCurrencies.equals("true")){
            System.out.println("unda amoshalos....");
            items.remove(CurrencyRate.ALL_CURRENCY);
        }
        else if (showOnlyCurrencies.equals("false")){
            for (String item : items) {
                if (item.equals(CurrencyRate.ALL_CURRENCY)){
                    return;
                }
            }
            items.add(CurrencyRate.ALL_CURRENCY);
        }
    }
    
    public String getShowOnlyCurrencies(){
        return showOnlyCurrencies;
    }
}
