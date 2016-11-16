/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import ambroafb.general.Utils;
import ambroafb.general.mapeditor.MapEditorElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class ProductDiscount implements MapEditorElement {

    private final StringProperty days;
    private final StringProperty discountRate;
    private String delimiter = " : ";
    private int recId = 0;

    public ProductDiscount() {
        days = new SimpleStringProperty("");
        discountRate = new SimpleStringProperty("");
    }

//    // It is needed for MapEditor.
//    public ProductDiscount(String days, String discount) {
//        this.days = days;
//        this.discountRate = discount;
//    }
    
    public int getDays(){
        return Utils.getIntValueFor(days.get());
    }
    
    public double getDiscountRate(){
        return Utils.getDoubleValueFor(discountRate.get());
    }

    @JsonIgnore
    public String getDelimiter(){
        return delimiter;
    } 
    
    public int getRecId(){
        return recId;
    }
    
    public void setDays(int months){
        this.days.set("" + months);
    }
    
    public void setDiscountRate(double discount){
        this.discountRate.set("" + discount);
    }

    @JsonIgnore
    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false; // ???? other ragac momentshi null xdeba
        ProductDiscount otherDisc = (ProductDiscount) other;
        return getDays() == otherDisc.getDays() && getDiscountRate() == otherDisc.getDiscountRate();
    }
    
    public boolean compares(ProductDiscount other){
        return getDays() == other.getDays() && getDiscountRate() == other.getDiscountRate();
    }

    @Override
    public String toString() {
        return days.get() + delimiter + discountRate.get();
    }

    @Override @JsonIgnore
    public String getKey() {
        return days.get();
    }

    @Override @JsonIgnore
    public String getValue() {
        return discountRate.get();
    }

    @Override @JsonIgnore
    public void setKey(String key) {
        days.set(key);
    }

    @Override @JsonIgnore
    public void setValue(String value) {
        discountRate.set(value);
    }

    @Override
    public int compare(MapEditorElement other) {
        return days.get().compareTo(other.getKey());
    }
}
