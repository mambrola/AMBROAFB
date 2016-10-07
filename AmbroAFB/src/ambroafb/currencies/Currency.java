/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author dato
 */
public class Currency extends EditorPanelable {

    
    @AView.Column(title = "%date", width = "100")
    private final StringProperty createDate;
    
    private final ObjectProperty<LocalDate> dateProperty;
    
    @AView.Column(title = "%iso", width = "50")
    private final StringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty descrip;
    
    @AView.Column(width = "20")
    private final StringProperty symbol;
    
    public static final String ALL = "ALL";
    public static final String NOT_SHOW = "GEL";
    
    public Currency(){
        createDate = new SimpleStringProperty("");
        dateProperty = new SimpleObjectProperty<>();
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(this);
        descrip = new SimpleStringProperty("");
        symbol = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
//                dateStr = DateConverter.getDayMonthnameYearBySpace(newValue);
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            createDate.set(dateStr);
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
    
    
    public static ArrayList<Currency> getAllFromDB(){
        try {
            String data = GeneralConfig.getInstance().getDBClient().select("currencies", new ConditionBuilder().build()).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Currency>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Currency.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
        
    }
    
    // DB methods:
    public static Currency getOneFromDB (int recId){
        Currency currency = new Currency();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from currencies where rec_id = " + recId);
            while(set.next()){
                currency.setRecId(set.getInt(1));
                currency.setIso(set.getString(2));
                currency.setSymbol(set.getString(6));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Currency.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return currency;
    }
    
    public static Product saveOneToDB(Product product){
        System.out.println("save one to DB... ??");
        return null;
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
    public String getCreateDate(){
        return createDate.get();
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
    public void setCreateDate(String date) {
        dateProperty.set(DateConverter.getInstance().parseDate(date));
//        String localDateStr;
//        try {
//            dateProperty.set(DateConverter.parseDateWithTimeWithoutMilisecond(date));
//            localDateStr = DateConverter.getDayMonthnameYearBySpace(dateProperty.get());
//        } catch(Exception ex) {
//            localDateStr = date;
//        }
//        this.createDate.set(localDateStr);
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
        return  getIso().equals(currency.getIso()) &&
                getDescrip().equals(currency.getDescrip()) &&
                getSymbol().equals(currency.getSymbol());
    }
    
    public boolean compares(Currency currencyBackup){
        return this.equals(currencyBackup);
    }
}
