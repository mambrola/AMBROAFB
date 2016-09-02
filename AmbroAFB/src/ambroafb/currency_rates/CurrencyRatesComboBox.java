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
    
    private final ObservableList<String> items = FXCollections.observableArrayList();
    
    public CurrencyRatesComboBox(){
        this.setItems(items);
        this.setVisible(false);
        giveCurrencyRatesFromDB(true);
    }
    
    private void giveCurrencyRatesFromDB(boolean showCategoryALL){
        new Thread(() -> {
            items.clear();
            if (showCategoryALL){
                items.add(CurrencyRate.ALL_CURRENCY);
            }
            CurrencyRate.getAllCurrencyFromDBTest().stream().forEach((rate) -> {
                items.add(rate);
            });
            Platform.runLater(() -> {
                this.setVisible(true);
                if (!items.isEmpty())
                    this.setValue(items.get(0));
            });
        }).start();
    }

    public void setShowCategoryALL(boolean showCategoryALL) {
        giveCurrencyRatesFromDB(showCategoryALL);
    }
}
