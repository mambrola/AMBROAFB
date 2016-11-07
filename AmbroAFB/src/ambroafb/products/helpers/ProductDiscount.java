/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import ambroafb.general.Utils;
import ambroafb.general.mapeditor.MapEditorElement;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author dato
 */
public class ProductDiscount implements MapEditorElement {

    private String days;
    private String discountRate;
    private String delimiter = " : ";
    private int recId = 0;

    public ProductDiscount() {
    }

//    // It is needed for MapEditor.
//    public ProductDiscount(String days, String discount) {
//        this.days = days;
//        this.discountRate = discount;
//    }
    
    public int getDays(){
        return Utils.getIntValueFor(days);
    }
    
    public double getDiscountRate(){
        return Utils.getDoubleValueFor(discountRate);
    }

    @JsonIgnore
    public String getDelimiter(){
        return delimiter;
    } 
    
    public int getRecId(){
        return recId;
    }
    
    public void setDays(int months){
        this.days = "" + months;
    }
    
    public void setDiscountRate(double discount){
        this.discountRate = "" + discount;
    }

    @JsonIgnore
    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public boolean equals(ProductDiscount other) {
        return getDays() == other.getDays() && getDiscountRate() == other.getDiscountRate();
    }
    

    @Override
    public String toString() {
        return days + delimiter + discountRate;
    }

    @Override @JsonIgnore
    public String getKey() {
        return days;
    }

    @Override @JsonIgnore
    public String getValue() {
        return discountRate;
    }

    @Override @JsonIgnore
    public void setKey(String key) {
        days = key;
    }

    @Override @JsonIgnore
    public void setValue(String value) {
        discountRate = value;
    }

    @Override
    public int compare(MapEditorElement other) {
        return days.compareTo(other.getKey());
    }
}
