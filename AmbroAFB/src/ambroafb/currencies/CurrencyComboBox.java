/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambroafb.general.GeneralConfig;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.json.JSONArray;
import org.json.JSONObject;

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
        
        try {
            JSONArray jsonArr = GeneralConfig.getInstance().getDBClient().select("basic_params", new ConditionBuilder().where().and("param", "=", "rates_basic_iso").condition().build());
            String removedIso = (String)((JSONObject)jsonArr.opt(0)).opt("value");
            
            ArrayList<Currency> filteredCurrencies = (ArrayList<Currency>)Currency.getAllFromDB().stream().filter((Currency currency) -> !currency.getIso().equals(removedIso)).collect(Collectors.toList());
            filteredCurrencies.sort((Currency c1, Currency c2) -> c1.getIso().compareTo(c2.getIso()));
            items.addAll(filteredCurrencies);
            
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(CurrencyComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setValue(allCurrensy);
        
        
    }
    

    public void setShowCategoryALL(boolean showCategoryALL) {
        if (!showCategoryALL){
            if (getItems().contains(allCurrensy))
                items.remove(allCurrensy);
        }
        else {
            if (!getItems().contains(allCurrensy))
                items.add(0, allCurrensy);
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
