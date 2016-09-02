/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambro.AView;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.interfaces.EditorPanelable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class DiscountOnCount extends EditorPanelable {

    @AView.Column(title = "%licenses_count", width = "50")
    private final StringProperty license_count;
    
    @AView.Column(title = "%sales_percent", width = "50")
    private final StringProperty discount_rate;
    
    public DiscountOnCount(){
        license_count = new SimpleStringProperty("0");
        discount_rate = new SimpleStringProperty("0");
    }
    
    // DB methods:
    public static ArrayList<EditorPanelable> getAllFromDBTest(){
        ArrayList<EditorPanelable> result = new ArrayList<>();
        
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from discounts_on_licenses_count; ");
            while (set.next()){
                DiscountOnCount discOnCount = new DiscountOnCount();
                discOnCount.setRecId(set.getInt(1));
                discOnCount.setLicense_count(set.getInt(2));
                discOnCount.setDiscount_rate(set.getInt(3));
                result.add(discOnCount);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public static DiscountOnCount getOneFromDB(int recId) {
        DiscountOnCount result = new DiscountOnCount();
        
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from discounts_on_licenses_count " +
                    " where rec_id = " + recId);
            while (set.next()) {                
                result.setLicense_count(set.getInt(2));
                result.setDiscount_rate(set.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscountOnCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    public static void saveOneToDB(DiscountOnCount discOnCount) {
        System.out.println("save one to DB... ??");
    }

    public static boolean deleteOneFromDB(int id) {
        return false;
    }
    
    // Properties:
    public StringProperty licenseCountProperty(){
        return license_count;
    }
    
    public StringProperty discountRateProperty(){
        return discount_rate;
    }
    
    
    // Getters:
    public int getLicense_count(){
        return getIntValueFor(license_count.get());
    }
    
    public int getDiscount_rate(){
        return getIntValueFor(discount_rate.get());
    }
    
    private int getIntValueFor(String str){
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception ex){ }
        return result;
    }
    
    
    // Settres:
    public void setLicense_count(int count){
        this.license_count.set("" + count);
    }
    
    public void setDiscount_rate(int rate){
        this.discount_rate.set("" + rate);
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
        setRecId(clone.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        DiscountOnCount discCount = (DiscountOnCount) other;
        setLicense_count(discCount.getLicense_count());
        setDiscount_rate(discCount.getDiscount_rate());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }

    public boolean compares(DiscountOnCount discCountBackup) {
        return  getLicense_count() == discCountBackup.getLicense_count() &&
                getDiscount_rate() == discCountBackup.getDiscount_rate();
    }
    
}
