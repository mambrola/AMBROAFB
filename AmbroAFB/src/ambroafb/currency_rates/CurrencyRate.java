/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.DateConverter;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {

    @AView.Column(title = "%date", width = "100")
    private final StringProperty date;
    @AView.Column(title = "%count", width = "50")
    private final StringProperty count;
    @AView.Column(title = "%iso", width = "50")
    private final StringProperty iso;
    private ObjectProperty<Currency> currency;
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty currDescrip;
    @AView.Column(title = "%rate", width = "80")
    private final StringProperty rate;
    
    private final ObjectProperty<LocalDate> dateProperty;
    
    public CurrencyRate(){
        dateProperty = new SimpleObjectProperty<>();
        date = new SimpleStringProperty("");
        count = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>();
        currDescrip = new SimpleStringProperty("");
        rate = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getDayMonthnameYearBySpace(newValue);
            }
            date.set(dateStr);
        });
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            iso.unbind();
            if (currency.get() != null){
                iso.bind(currency.get().isoProperty());
                currDescrip.bind(Bindings.when(currency.isNotNull()).then(currency.get().descripProperty()).otherwise("")); //(GeneralConfig.getInstance().getCurrentLocal().getLanguage().equals("ka")) ? descrip_first : (GeneralConfig.getInstance().getCurrentLocal().getLanguage().equals("en")) ? descrip_second : descrip_default;
            }
        });
    }

    public static ArrayList<CurrencyRate> getFilteredFromDBTest(JSONObject filterJson){
        ArrayList<CurrencyRate> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                String query = "select currency_rates.rec_id, currency_rates.iso, currency_rates.count, currency_rates.date, currency_rates.rate, " +
                                    " currencies.iso, currencies.descrip_first, currencies.descrip_default, currencies.descrip_second, currencies.symbol " +
                                " from currency_rates " +
                                    " left join currencies " + 
                                        " on currency_rates.iso = currencies.iso " +
                                    " where date > '" + filterJson.get("dateBigger") + "' " + 
                                        " and date < '" + filterJson.getString("dateLess") + "' " +
                                        " order by date desc, currency_rates.iso;";
                if (!filterJson.getString("currency").equals(Currency.ALL)){
                    String delimiter = "where ";
                    int whereIndex = query.indexOf(delimiter);
                    String leftPart =  query.substring(0, whereIndex + delimiter.length());
                    
                    String rightPart = query.substring(whereIndex + delimiter.length());

                    query = leftPart + " currency_rates.iso = '" + filterJson.getString("currency") + "' and " + rightPart;
                }
                ResultSet set = stmt.executeQuery(query);
                while (set.next()){
                    CurrencyRate currRate = new CurrencyRate();
                    currRate.setRecId(set.getInt(1));
                    currRate.setCount(set.getInt(3));
                    currRate.setDate(set.getString(4));
                    currRate.setRate(set.getDouble(5));
                    Currency currency = new Currency();
                    currency.setIso(set.getString(6));
                    currency.setDescrip_first(set.getString(7));
                    currency.setDescrip_default(set.getString(8));
                    currency.setDescrip_second(set.getString(9));
                    currency.setSymbol(set.getString(10));
                    currRate.setCurrency(currency);
                    result.add(currRate);
                }
            } catch (SQLException | JSONException ex) {
                Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
            
    public static CurrencyRate getOneFromDB (int recId){
        CurrencyRate result = new CurrencyRate();
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                ResultSet set = stmt.executeQuery("select currency_rates.rec_id, currency_rates.count, currency_rates.date, currency_rates.rate, " +
                                                  " currencies.iso, currencies.descrip_first, currencies.descrip_default, currencies.descrip_second, currencies.symbol " +
                                                  " from currency_rates " +
                                                  " left join currencies " +
                                                  " on currency_rates.iso = currencies.iso " +
                                                  " where currency_rates.rec_id = " + recId);
                while (set.next()){
                    result.setRecId(set.getInt(1));
                    result.setCount(set.getInt(2));
                    result.setDate(set.getString(3));
                    result.setRate(set.getDouble(4));
                    Currency currency = new Currency();
                    currency.setIso(set.getString(5));
                    currency.setDescrip_first(set.getString(6));
                    currency.setDescrip_default(set.getString(7));
                    currency.setDescrip_second(set.getString(8));
                    currency.setSymbol(set.getString(9));
                    result.setCurrency(currency);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public static Product saveOneToDB(CurrencyRate currencyRate){
        System.out.println("saveOneToDB... ??");
        return null;
    }
    
    public static boolean deleteOneFromDB(int recId){
        System.out.println("delete from DB... ??");
        return false;
    }
    
    
    // Properties:
    public ObjectProperty<LocalDate> dateProperty(){
        return dateProperty;
    }
    
    public StringProperty countProperty(){
        return count;
    }
    
    public StringProperty isoProperty() {
        return iso;
    }
    
    public ObjectProperty<Currency> currencyProperty(){
        return currency;
    }
    
    public StringProperty currentDescrip(){
        return currDescrip;
    }

    public StringProperty rateProperty() {
        return rate;
    }
    
    
    // Getters:
    public String getDate() {
        return date.get();
    }
    
    public int getCount(){
        return Utils.getIntValueFor(count.get());
    }
    
    public Currency getCurrency(){
        return currency.get();
    }
            
    public double getRate(){
        return Utils.getDoubleValueFor(rate.get());
    }
    
    
    // Setters:
    public void setDate(String date) {
        String localDateStr;
        try {
            dateProperty.set(DateConverter.getLocalDateFor(date)); // converter.fromString(date);
            localDateStr = DateConverter.getDayMonthnameYearBySpace(dateProperty.get()); // converter.toString(localDate);
        } catch(Exception ex) {
            localDateStr = date;
        }
        this.date.set(localDateStr);
    }
    
    public void setCount(int count){
        this.count.set("" + count);
    }
    
    public void setCurrency(Currency currency){
        this.currency.set(currency);
    }
    
    public void setRate(double rate){
        this.rate.set("" + rate);
    }
    

    @Override
    public CurrencyRate cloneWithoutID() {
        CurrencyRate clone = new CurrencyRate();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public CurrencyRate cloneWithID() {
        CurrencyRate clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        CurrencyRate currencyRate = (CurrencyRate) other;
        setDate(currencyRate.getDate());
        setCount(currencyRate.getCount());
        setCurrency(currencyRate.getCurrency());
        setRate(currencyRate.getRate());
        
    }

    @Override
    public String toStringForSearch() {
        return getDate();
    }
    
    /**
     * Method compares two CurrencyRates.
     * @param currencyRateBackup Other currencyRate
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    public boolean compares(CurrencyRate currencyRateBackup) {
        return  getDate().equals(currencyRateBackup.getDate()) &&
                getCount() == currencyRateBackup.getCount()    &&
                getCurrency().compares(currencyRateBackup.getCurrency())   && 
                currentDescrip().get().equals(currencyRateBackup.currentDescrip().get()) &&
                getRate() == currencyRateBackup.getRate();
    }
    
}
