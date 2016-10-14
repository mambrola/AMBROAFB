/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class DiscountOnCount extends EditorPanelable {

    @AView.Column(title = "%licenses_count", width = "200", styleClass = "textRight")
    private final StringProperty licenseCount;
    
    @AView.Column(title = "%sales_percent", width = "100", styleClass = "textRight")
    private final StringProperty discountRate;
    
    public DiscountOnCount(){
        licenseCount = new SimpleStringProperty("");
        discountRate = new SimpleStringProperty("");
    }
    
    // DB methods:
    public static ArrayList<EditorPanelable> getAllFromDB(){
        try {
            String data = GeneralConfig.getInstance().getDBClient().select("discounts_on_licenses_count", new ConditionBuilder().build()).toString();
            
            System.out.println("discount on counts all: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<DiscountOnCount>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static DiscountOnCount getOneFromDB(int recId) {
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select("discounts_on_licenses_count", conditionBuilder.build());
            
            String currencyData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(currencyData, DiscountOnCount.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static DiscountOnCount saveOneToDB(DiscountOnCount discOnCount) {
        System.out.println("axlaaaa ");
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            
            System.out.println("gasagzavni: " + writer.writeValueAsString(discOnCount));
            
            JSONObject discOnCountJson = new JSONObject(writer.writeValueAsString(discOnCount));
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newDiscOnCount = dbClient.callProcedureAndGetAsJson("general_insert_update", "discounts_on_licenses_count", dbClient.getLang(), discOnCountJson).getJSONObject(0);
//            return mapper.readValue(newDiscOnCount.toString(), DiscountOnCount.class);
            return null;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean deleteOneFromDB(int id) {
        System.out.println("delete from DB... ??");
        return false;
    }
    
    // Properties:
    public StringProperty licenseCountProperty(){
        return licenseCount;
    }
    
    public StringProperty discountRateProperty(){
        return discountRate;
    }
    
    
    // Getters:
    public int getLicenseCount(){
        return Utils.getIntValueFor(licenseCount.get());
    }
    
    public int getDiscountRate(){
        return Utils.getIntValueFor(discountRate.get());
    }
    
    
    // Settres:
    public void setLicenseCount(int count){
        this.licenseCount.set("" + count);
    }
    
    public void setDiscountRate(int rate){
        this.discountRate.set("" + rate);
    }
    
    
    @Override
    public DiscountOnCount cloneWithoutID() {
        DiscountOnCount clone = new DiscountOnCount();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public DiscountOnCount cloneWithID() {
        DiscountOnCount clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        DiscountOnCount discCount = (DiscountOnCount) other;
        setLicenseCount(discCount.getLicenseCount());
        setDiscountRate(discCount.getDiscountRate());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }

    public boolean compares(DiscountOnCount discCountBackup) {
        return  getLicenseCount() == discCountBackup.getLicenseCount() &&
                getDiscountRate() == discCountBackup.getDiscountRate();
    }
    
}
