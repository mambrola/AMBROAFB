/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
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
    @AView.Column(title = "%descrip", width = "50")
    private final StringProperty iso;
    @AView.Column(title = "%descrip", width = "150")
    private final StringProperty currDescrip;
    @AView.Column(title = "%rate", width = "80")
    private final StringProperty rate;
    
    public static final String ALL_CURRENCY = "ALL";
    private final StringProperty descrip_ka;
    private final StringProperty descrip_en;
    private final ObjectProperty<LocalDate> dateProperty;
    
    public CurrencyRate(){
        dateProperty = new SimpleObjectProperty<>();
        date = new SimpleStringProperty("");
        count = new SimpleStringProperty("1");
        iso = new SimpleStringProperty("");
        descrip_ka = new SimpleStringProperty("");
        descrip_en = new SimpleStringProperty("");
        rate = new SimpleStringProperty("0");
        
        currDescrip = (GeneralConfig.getInstance().getCurrentLocal().getLanguage().equals("ka")) ? descrip_ka : descrip_en;
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getDayMonthnameYearBySpace(newValue);
            }
            date.set(dateStr);
        });
    }

    public static ArrayList<String> getAllCurrencyFromDBTest() {
        ArrayList<String> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                ResultSet set = stmt.executeQuery("select iso from currencies where iso != 'GEL';");
                while (set.next()){
                    result.add(set.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public static ArrayList<CurrencyRate> getFilteredFromDBTest(JSONObject filterJson){
        ArrayList<CurrencyRate> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                String query = "select currency_rates.rec_id, currency_rates.iso, currency_rates.count, currency_rates.date, currency_rates.rate, " +
                                    " currencies.iso, currencies.descrip_ka, currencies.descrip_en " +
                                " from currency_rates " +
                                    " left join currencies " + 
                                        " on currency_rates.iso = currencies.iso " +
                                    " where date > '" + filterJson.get("dateBigger") + "' " + 
                                        " and date < '" + filterJson.getString("dateLess") + "' " +
                                        " order by date desc, currency_rates.iso;";
                if (!filterJson.getString("currency").equals(ALL_CURRENCY)){
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
                    currRate.setIso(set.getString(2));
                    currRate.setCount(set.getInt(3));
                    currRate.setDate(set.getString(4));
                    currRate.setRate(set.getDouble(5));
                    currRate.setDescrip_ka(set.getString(7));
                    currRate.setDescrip_en(set.getString(8));
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
                ResultSet set = stmt.executeQuery("select * from currency_rates where rec_id = " + recId);
                while (set.next()){
                    result.setRecId(set.getInt(1));
                    result.setIso(set.getString(2));
                    result.setCount(set.getInt(3));
                    result.setDate(set.getString(4));
                    result.setRate(set.getDouble(5));
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
    
    public StringProperty descripExpression(){
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
        int result = 0;
        try {
            result = Integer.parseInt(count.get());
        } catch (Exception ex){ }
        return result;
    }
            
    public String getIso(){
        return iso.get();
    }
    
    public String getDescrip_ka(){
        return descrip_ka.get();
    }
    
    public String getDescrip_en(){
        return descrip_en.get();
    }
    
    public double getRate(){
        double result = 0;
        try {
            result = Double.parseDouble(rate.get());
        } catch (Exception ex){ }
        return result;
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
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setDescrip_ka(String descrip){
        this.descrip_ka.set(descrip);
    }
    
    public void setDescrip_en(String descrip){
        this.descrip_en.set(descrip);
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
        setIso(currencyRate.getIso());
        setRate(currencyRate.getRate());
        setDate(currencyRate.getDate());
        setCount(currencyRate.getCount());
        
    }

    @Override
    public String toStringForSearch() {
        return getDate().concat(getIso()).concat("" + getRate()).concat(currDescrip.get());
    }
    
    /**
     * Method compares two CurrencyRates.
     * @param currencyRateBackup Other currencyRate
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    public boolean compares(CurrencyRate currencyRateBackup) {
        return  getIso().equals(currencyRateBackup.getIso())   && 
                getDate().equals(currencyRateBackup.getDate()) &&
                getRate() == currencyRateBackup.getRate()      &&
                getCount() == currencyRateBackup.getCount();
    }
    
}
