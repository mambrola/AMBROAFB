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
    
    public CurrencyRatesComboBox(){
        giveCurrencyRatesFromDB();
        this.getItems().add(0, "All");
    }
    
    private void giveCurrencyRatesFromDB(){
        new Thread(() -> {
            CurrencyRate.getAllCurrencyFromDBTest().stream().forEach((rate) -> {
                getItems().add(rate);
            });
        }).start();
    }

    public void showOnlyCurrencies(){
        getItems().remove("All");
    }
}
