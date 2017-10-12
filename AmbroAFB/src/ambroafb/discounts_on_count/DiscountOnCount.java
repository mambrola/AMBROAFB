/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
 import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class DiscountOnCount extends EditorPanelable {

    @AView.Column(title = "%licenses_count_min", width = "120", styleClass = "textRight")
    private final StringProperty licenseCount;
    
    @AView.Column(title = "%sales_percent", width = "100", styleClass = "textRight")
    private final StringProperty discountRate;
    
    @JsonIgnore
    private static final String DB_TABLE_NAME = "discounts_on_licenses_count";
    
    public DiscountOnCount(){
        licenseCount = new SimpleStringProperty("");
        discountRate = new SimpleStringProperty("");
    }
    
    // DB methods:
    public static ArrayList<DiscountOnCount> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<DiscountOnCount> discounts = DBUtils.getObjectsListFromDB(DiscountOnCount.class, DB_TABLE_NAME, params);
        discounts.sort((DiscountOnCount d1, DiscountOnCount d2) -> d2.getRecId() - d1.getRecId());
        return discounts;
    }
    
    public static DiscountOnCount getOneFromDB(int recId) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(DiscountOnCount.class, DB_TABLE_NAME, params);
    }

    public static DiscountOnCount saveOneToDB(DiscountOnCount discOnCount) {
        System.out.println("save one to DB???");
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
    
    public String getDiscountRate(){
        return discountRate.get();
    }
    
    
    // Settres:
    public void setLicenseCount(int count){
        this.licenseCount.set("" + count);
    }
    
    public void setDiscountRate(String rate){
        this.discountRate.set(rate);
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

    /**
     *
     * @param backup
     * @return
     */
    @Override
    public boolean compares(EditorPanelable backup) {
        DiscountOnCount discCountBackup = (DiscountOnCount) backup;
        return  getLicenseCount() == discCountBackup.getLicenseCount() &&
                getDiscountRate().equals(discCountBackup.getDiscountRate());
    }
    
}
