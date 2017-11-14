/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count;

import ambro.AView;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class DiscountOnCount extends EditorPanelable {

    @AView.Column(title = "%licenses_count_min", width = "120", styleClass = "textRight")
    private final StringProperty licenseCount;
    
    @AView.Column(title = "%sales_percent", width = "100", styleClass = "textRight")
    private final StringProperty discountRate;
    

    public DiscountOnCount(){
        licenseCount = new SimpleStringProperty("");
        discountRate = new SimpleStringProperty("");
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
