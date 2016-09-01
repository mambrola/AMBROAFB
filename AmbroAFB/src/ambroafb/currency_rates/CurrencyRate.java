/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {

    @AView.Column(title = "%date", width = "100")
//    private LocalDate date;
    private final StringProperty date;
    @AView.Column(title = "%descrip", width = "50")
    private final StringProperty iso;
    @AView.Column(title = "%rate", width = "80")
    private final StringProperty rate;
    
//    private final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private final LocalDateStringConverter converter = new LocalDateStringConverter(formater, pattern);
    
    public CurrencyRate(){
        date = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        rate = new SimpleStringProperty("0");
    }

    public static ArrayList<String> getAllCurrencyFromDBTest() {
        ArrayList<String> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                ResultSet set = stmt.executeQuery("select iso from currencies where iso != 'GEL'");
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
                String query = "select * from currency_rates " +
                            "where date > '" + filterJson.get("dateBigger") + "' " + 
                                " and date < '" + filterJson.getString("dateLess") + "'";
                if (!filterJson.getString("currency").equals("All")){
                    String delimiter = "where ";
                    int whereIndex = query.indexOf(delimiter);
                    String leftPart =  query.substring(0, whereIndex + delimiter.length());
                    
                    String rightPart = query.substring(whereIndex + delimiter.length());

                    query = leftPart + " iso = '" + filterJson.getString("currency") + "' and " + rightPart;
                }
                ResultSet set = stmt.executeQuery(query);
                while (set.next()){
                    CurrencyRate currRate = new CurrencyRate();
                    currRate.setRecId(set.getInt(1));
                    currRate.setIso(set.getString(2));
                    currRate.setRate(set.getDouble(3));
                    currRate.setDate(set.getString(4));
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
                    result.setRate(set.getDouble(3));
                    result.setDate(set.getString(4));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public static Product saveOneToDB(Product product){
        System.out.println("saveOneToDB... ??");
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete from DB... ??");
        return false;
    }
    
    
    // Properties:
    public StringProperty dateProperty(){
        return date;
    }
    
    public StringProperty isoProperty() {
        return iso;
    }

    public StringProperty rateProperty() {
        return rate;
    }

    
    // Getters:
    public String getIso(){
        return iso.get();
    }
    
    public double getRate(){
        return Double.parseDouble(rate.get());
    }
    
    public String getDate() {
        return date.get();
//        return converter.toString(date);
    }
    
    
    // Setters:
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setRate(double rate){
        this.rate.set("" + rate);
    }
    
    public void setDate(String date) {
        this.date.set(date);
//        this.date = converter.fromString(date);
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
    }

    @Override
    public String toStringForSearch() {
        return getIso().concat("" + getRate()).concat(getDate());
    }
    
    /**
     * Method compares two CurrencyRates.
     * @param currencyRateBackup Other currencyRate
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    public boolean compares(CurrencyRate currencyRateBackup) {
        return  getIso().equals(currencyRateBackup.getIso()) && 
                getDate().equals(currencyRateBackup.getDate()) &&
                getRate() == currencyRateBackup.getRate();
    }
    
    private boolean equalsDates(CurrencyRate currencyRateBackup){
        
        return ;
    }
}
