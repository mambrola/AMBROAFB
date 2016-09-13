/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class CurrencyComboBox extends ComboBox<Currency>{
    
    private final ObservableList<Currency> items = FXCollections.observableArrayList();
    private final Currency allCurrensy;
    
    public CurrencyComboBox(){
        this.setItems(items);
        allCurrensy = new Currency();
        allCurrensy.setIso(Currency.ALL);
        items.add(allCurrensy);
        
        Currency.getAllFromDBTest().stream().forEach((currency) -> {
            items.add(currency);
        });
        
        this.setValue(allCurrensy);
        System.out.println("comboBox kontsruqtori");
    }
    

    public void setShowCategoryALL(boolean showCategoryALL) {
        if (!showCategoryALL){
            if (getItems().contains(allCurrensy))
                getItems().remove(allCurrensy);
        }
        else {
            if (!getItems().contains(allCurrensy))
                getItems().add(0, allCurrensy);
        }
    }
    
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
}
