/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.DateConverter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import ambroafb.general.interfaces.TableColumnWidths;


/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {
    
    @AView.Column(title = "%date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    private final StringProperty date;
    @AView.Column(title = "%count", width = "50", styleClass = "textCenter")
    private final StringProperty count;
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final StringProperty iso;
    @AView.Column(title = "%rate", width = "80", styleClass = "textRight")
    private final StringProperty rate;
    
    private ObjectProperty<Currency> isoProperty;
    private final ObjectProperty<LocalDate> dateProperty;
    private static final Map<String, Currency> currencies = new HashMap<>();
    
    public CurrencyRate(){
        dateProperty = new SimpleObjectProperty<>();
        date = new SimpleStringProperty("");
        count = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        isoProperty = new SimpleObjectProperty<>();
        rate = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            date.set(dateStr);
        });
        
        isoProperty.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            iso.unbind();
            if (iso.get() != null){
                iso.bind(isoProperty.get().isoProperty());
            }
        });
    }

    public static ArrayList<CurrencyRate> getFilteredFromDB(JSONObject filterJson){
        try {
            String from = filterJson.optString("dateBigger");
            String to = filterJson.optString("dateLess");
            String currency = filterJson.optString("currency");
            WhereBuilder whereBuilder = new ConditionBuilder().where()
                    .and("date", ">=", from).and("date", "<=", to);
            if (!currency.equals(Currency.ALL)){
                whereBuilder = whereBuilder.and("iso", "=", currency);
            }
            JSONObject params = whereBuilder.condition().build();
            String data = GeneralConfig.getInstance().getDBClient().select("rates_whole", params).toString();
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<CurrencyRate>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    
    public static CurrencyRate getOneFromDB (int recId){
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select("rates_whole", conditionBuilder.build());
            String currencyData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(currencyData, CurrencyRate.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static CurrencyRate saveOneToDB(CurrencyRate currencyRate){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            
            System.out.println("currencyRate. saveOneToDB. writer.writeValueAsString(currencyRate): " + writer.writeValueAsString(currencyRate));
            
            JSONObject rateJson = new JSONObject(writer.writeValueAsString(currencyRate));
            JSONObject newRate = GeneralConfig.getInstance().getDBClient().insertUpdate("rates", rateJson);
            return mapper.readValue(newRate.toString(), CurrencyRate.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public ObjectProperty<Currency> currencyProperty(){
        return isoProperty;
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
    
    public String getIso(){
        return iso.get();
    }
            
    public double getRate(){
        return Utils.getDoubleValueFor(rate.get());
    }
    
    // Setters:
    public void setDate(String date) {
        dateProperty.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setCount(int count){
        this.count.set("" + count);
    }
    
    
    public void setIso(String iso){
        Currency currency = currencies.get(iso);
        try {
            if (currency == null) {
                JSONArray data = GeneralConfig.getInstance().getDBClient().select("currencies", new ConditionBuilder().where().and("iso", "=", iso).condition().build());
                if (data.length() > 0) {
                    String currencyData = data.opt(0).toString();
                    ObjectMapper mapper = new ObjectMapper();
                    currency = mapper.readValue(currencyData, Currency.class);
                    currencies.put(iso, currency);
                }
            }
            this.isoProperty.set(currency);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        CurrencyRate source = (CurrencyRate) other;
        this.setDate(source.getDate());
        this.setCount(source.getCount());
        this.setIso(source.getIso());
        this.setRate(source.getRate());
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
        return  this.getDate().equals(currencyRateBackup.getDate()) &&
                this.getCount() == currencyRateBackup.getCount()    &&
                this.getIso().equals(currencyRateBackup.getIso())   &&
                this.getRate() == currencyRateBackup.getRate();
    }
    
}
