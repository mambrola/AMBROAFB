/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class CurrencyRatesComboBox extends ComboBox<String>{
    
    private final String allCurrencies = "ALL";
    private String showOnlyCurrencies;
    
    private final ObservableList<String> elements = FXCollections.observableArrayList();
    
    public CurrencyRatesComboBox(){
        this.setItems(elements);
        giveCurrencyRatesFromDB();
    }
    
    private void giveCurrencyRatesFromDB(){
        new Thread(() -> {
            CurrencyRate.getAllCurrencyFromDBTest().stream().forEach((rate) -> {
                elements.add(rate);
            });
            elements.add(0, allCurrencies);
        }).start();
    }

    
    public void setShowOnlyCurrencies(String showOnlyCurrencies){
        this.showOnlyCurrencies = showOnlyCurrencies;
        if (showOnlyCurrencies.equals("true")){
            getItems().remove(allCurrencies);
        }
        else {
            getItems().add(allCurrencies);
        }
    }
    
    public String getShowOnlyCurrencies(){
        return showOnlyCurrencies;
    }
}
