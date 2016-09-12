/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    @AView.Column(title = "%iso", width = "50")
    private final StringProperty iso;
    private ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty descrip;
    private final StringProperty descrip_first;
    private final StringProperty descrip_default;
    private final StringProperty descrip_second;
    
    @AView.Column(width = "20")
    private final StringProperty symbol;
    
    public static final String ALL = "ALL";
    
    public Currency(){
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>();
        descrip_first = new SimpleStringProperty("");
        descrip_default = new SimpleStringProperty("");
        descrip_second = new SimpleStringProperty("");
        String lang = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
        descrip = (lang.equals("ka")) ? descrip_first : (lang.equals("en")) ? descrip_second : descrip_default;
        symbol = new SimpleStringProperty("");
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
//            iso.unbind();
            if (currency.get() != null){
                iso.set(currency.get().isoProperty().get());
                descrip.set(currency.get().descripProperty().get());
            }
        });
    }
    
    
    public static ArrayList<Currency> getAllFromDBTest(){
        ArrayList<Currency> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from currencies ");
            while(set.next()){
                Currency curr = new Currency();
                curr.setRecId(set.getInt(1));
                curr.setIso(set.getString(2));
                curr.setDescrip_first(set.getString(3));
                curr.setDescrip_default(set.getString(4));
                curr.setDescrip_second(set.getString(5));
                curr.setSymbol(set.getString(6));
                result.add(curr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Currency.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
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
                currency.setDescrip_first(set.getString(3));
                currency.setDescrip_default(set.getString(4));
                currency.setDescrip_second(set.getString(5));
                currency.setDescrip_second(set.getString(5));
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
    public String getIso(){
        return iso.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public String getDescrip_first(){
        return descrip_first.get();
    }
    
    public String getDescrip_default(){
        return descrip_default.get();
    }
    
    public String getDescrip_second(){
        return descrip_second.get();
    }
    
    public String getSymbol(){
        return symbol.get();
    }
    
    
    // Setters:
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setDescrip_first(String descrip){
        this.descrip_first.set(descrip);
    }
    
    public void setDescrip_default(String descrip){
        this.descrip_default.set(descrip);
    }
    
    public void setDescrip_second(String descrip){
        this.descrip_second.set(descrip);
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
