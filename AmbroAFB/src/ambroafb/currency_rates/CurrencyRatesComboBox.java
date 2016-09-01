/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class CurrencyRatesComboBox extends ComboBox<String>{
    
    private final String allCurrencies = "All";
    private String showOnlyCurrencies;
    
    public CurrencyRatesComboBox(){
        giveCurrencyRatesFromDB();
        this.getItems().add(0, allCurrencies);
    }
    
    private void giveCurrencyRatesFromDB(){
        new Thread(() -> {
            CurrencyRate.getAllCurrencyFromDBTest().stream().forEach((rate) -> {
                getItems().add(rate);
            });
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
