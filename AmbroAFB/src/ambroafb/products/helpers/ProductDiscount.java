/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import ambroafb.general.Utils;

/**
 *
 * @author dato
 */
public class ProductDiscount {

    private String months;
    private String discount;
    private String delimiter = " : ";

    public ProductDiscount() {
    }

    // It is needed for MapEditor.
    public ProductDiscount(String months, String discount) {
        this.months = months;
        this.discount = discount;
    }
    
    public int getMonths(){
        return Utils.getIntValueFor(months);
    }
    
    public double getDiscount(){
        return Utils.getDoubleValueFor(discount);
    }

    public String getDelimiter(){
        return delimiter;
    } 
    
    public void setMonths(int months){
        this.months = "" + months;
    }
    
    public void setDiscount(double discount){
        this.discount = "" + discount;
    }

    public void setDelimiter(String delimiter){
        this.delimiter = delimiter;
    }
    
    public boolean equals(ProductDiscount other) {
        return getMonths() == other.getMonths() && getDiscount() == other.getDiscount();
    }
    

    @Override
    public String toString() {
        return months + delimiter + discount;
    }
}
