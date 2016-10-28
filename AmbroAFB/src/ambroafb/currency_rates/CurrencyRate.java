/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.currency_rates.filter.CurrencyRateFilterModel;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;
import ambroafb.general.interfaces.TableColumnWidths;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *
 * @author dato
 */
public class CurrencyRate extends EditorPanelable {
    
    @AView.Column(title = "%date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    private final StringProperty dateForColumn;
    @JsonIgnore
    private final ObjectProperty<LocalDate> dateProperty;
    
    @AView.Column(title = "%count", width = "50", styleClass = "textCenter")
    private final StringProperty count;
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final StringProperty iso;
    @JsonIgnore
    private final ObjectProperty<Currency> currency;
    @AView.Column(title = "%rate", width = "80", styleClass = "textRight")
    private final StringProperty rate;
    
    @JsonIgnore
    private static final String DB_TABLE_NAME = "rates";
    @JsonIgnore
    private static final String DB_VIEW_NAME = "rates_whole";
    
    public CurrencyRate(){
        dateProperty = new SimpleObjectProperty<>();
        dateForColumn = new SimpleStringProperty("");
        count = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(new Currency());
        rate = new SimpleStringProperty("");
        
        dateProperty.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            System.out.println("in date listener");
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            dateForColumn.set(dateStr);
        });
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            rebindIso();
        });
        rebindIso();
    }
    
    private void rebindIso(){
        iso.unbind();
        if (currency.get() != null){
            iso.bind(currency.get().isoProperty());
        }
    }
    
    public static ArrayList<CurrencyRate> getFilteredFromDB(FilterModel model) {
        CurrencyRateFilterModel currencyRateFilterModel = (CurrencyRateFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where()
                .and("date", ">=", currencyRateFilterModel.getFromDateForDB())
                .and("date", "<=", currencyRateFilterModel.getToDateForDB());
        if (!currencyRateFilterModel.isSelectedCurrencyALL()) {
            whereBuilder.and("iso", "=", currencyRateFilterModel.getSelectedCurrency().getIso());
        }
        JSONObject params = whereBuilder.condition().build();
        return DBUtils.getObjectsListFromDB(CurrencyRate.class, DB_VIEW_NAME, params);
    }

    public static CurrencyRate getOneFromDB (int recId){
        ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
        JSONObject params = conditionBuilder.build();
        return DBUtils.getObjectFromDB(CurrencyRate.class, DB_VIEW_NAME, params);
        
//        try {
//            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
//            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, conditionBuilder.build());
//            String currencyData = data.opt(0).toString();
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(currencyData, CurrencyRate.class);
//        } catch (IOException | AuthServerException ex) {
//            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
    
    public static CurrencyRate saveOneToDB(CurrencyRate currencyRate){
        if (currencyRate == null) return null;
        return DBUtils.saveObjectToDBSimple(currencyRate, DB_TABLE_NAME);
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
//            
//            System.out.println("currencyRate. saveOneToDB. writer.writeValueAsString(currencyRate): " + writer.writeValueAsString(currencyRate));
//            
//            JSONObject rateJson = new JSONObject(writer.writeValueAsString(currencyRate));
//            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            JSONObject newRate = dbClient.callProcedureAndGetAsJson("general_insert_update_simpledate", DB_TABLE_NAME, dbClient.getLang(), rateJson).getJSONObject(0);
//            return mapper.readValue(newRate.toString(), CurrencyRate.class);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException | AuthServerException | JSONException ex) {
//            Logger.getLogger(CurrencyRate.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
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
        return currency;
    }
    
    public StringProperty rateProperty() {
        return rate;
    }
    
    
    // Getters:
    @JsonIgnore
    public String getDateColumn(){
        return dateForColumn.get();
    }
    
    public String getDate() {
        System.out.println("dateProp: " + dateProperty);
        System.out.println("dateProp.get(): " + dateProperty.get());
        return (dateProperty.get() == null) ? "" : dateProperty.get().toString();
    }
    
    public int getCount(){
        return Utils.getIntValueFor(count.get());
    }
    
    public String getIso(){
        return currency.get().getIso();
    }
            
    public double getRate(){
        return Utils.getDoubleValueFor(rate.get());
    }
    
    // Setters:
    public void setDate(String date) {
        dateProperty.set(DateConverter.getInstance().parseDate(date));
        System.out.println("ki mara date xo daeseta ?? date from json: " + date);
        System.out.println("date axla: " + dateProperty.get());
    }
    
    public void setCount(int count){
        this.count.set("" + count);
    }
    
    public void setIso(String iso) {
        this.currency.get().setIso(iso);
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
     * @param backup Other currencyRate
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    @Override
    public boolean compares(EditorPanelable backup) {
        CurrencyRate currencyRateBackup = (CurrencyRate) backup;
        return  this.getDate().equals(currencyRateBackup.getDate()) &&
                this.getCount() == currencyRateBackup.getCount()    &&
                this.getIso().equals(currencyRateBackup.getIso())   &&
                this.getRate() == currencyRateBackup.getRate();
    }
    
}
