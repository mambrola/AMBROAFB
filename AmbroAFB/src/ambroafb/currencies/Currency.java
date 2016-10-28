/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class Currency extends EditorPanelable {

    
    @AView.Column(title = "%date", width = "100")
    private final StringProperty createdDate;
    
    private final ObjectProperty<LocalDate> dateProperty;
    
    @AView.Column(title = "%iso", width = "50")
    private final StringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty descrip;
    
    @AView.Column(width = "20")
    private final StringProperty symbol;
    
    @JsonIgnore
    public static final String ALL = "ALL";
    @JsonIgnore
    private static final String DB_TABLE_NAME = "currencies";
    
    public Currency(){
        createdDate = new SimpleStringProperty("");
        dateProperty = new SimpleObjectProperty<>();
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(this);
        descrip = new SimpleStringProperty("");
        symbol = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            createdDate.set(dateStr);
        });
        
        // Bind components does not work for this case. Because DB methods calls setters ("bind" and also settter is conflicted couple). So listener also call setters to change currency values:
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            if (newValue != null){
                setIso(newValue.getIso());
                setDescrip(newValue.getDescrip());
                setSymbol(newValue.getSymbol());
            }
        });
        
    }
    
    
    // DB methods:
    public static ArrayList<Currency> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Currency.class, DB_TABLE_NAME, params);
        
    }
    
    public static Currency getOneFromDB (int recId){
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    
    public static Currency getOneFromDB (String iso){
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("iso", "=", iso).condition();
        return getOneFromDBHelper(conditionBuilder);
    }
    
    private static Currency getOneFromDBHelper(ConditionBuilder conditionBuilder){
        JSONObject params = conditionBuilder.build();
        return DBUtils.getObjectFromDB(Currency.class, DB_TABLE_NAME, params);
    }
    
    public static Currency saveOneToDB(Currency currency){
        if (currency == null) return null;
        return DBUtils.saveObjectToDBSimple(currency, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete from db...??");
        return false;
    }
    
    // Properties:
    public ObjectProperty<LocalDate> dateProperty(){
        return dateProperty;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public ObjectProperty<Currency> currencyProperty(){
        return currency;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty symbolProperty(){
        return symbol;
    }
    
    
    // Getters:
    public String getCreatedDate(){
        return createdDate.get();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public String getSymbol(){
        return symbol.get();
    }
    
    
    // Setters:
    public void setCreatedDate(String date) {
        this.dateProperty.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setSymbol(String symbol){
        this.symbol.set(symbol);
    }
    
    
    @Override
    public Currency cloneWithoutID() {
        Currency clone = new Currency();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Currency cloneWithID() {
        Currency clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Currency otherCurrency = (Currency) other;
        setIso(otherCurrency.getIso());
        setDescrip(otherCurrency.getDescrip());
        setSymbol(otherCurrency.getSymbol());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
    @Override
    public String toString(){
        return getIso();
    }
    
    public boolean equals(Currency currency){
        return  this.getIso().equals(currency.getIso());
    }
    
    /**
     *
     * @param backup
     * @return
     */
    @Override
    public boolean compares(EditorPanelable backup){
        Currency currencyBackup = (Currency) backup;
        return  this.getIso().equals(currencyBackup.getIso()) &&
                this.getDescrip().equals(currencyBackup.getDescrip()) &&
                this.getSymbol().equals(currencyBackup.getSymbol());
    }
}
