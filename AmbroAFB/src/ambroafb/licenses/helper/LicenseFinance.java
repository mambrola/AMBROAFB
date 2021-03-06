/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.helper;

/**
 *
 * @author dato
 */
public class LicenseFinance {
    
    public int productId;
    public String articul;
    public String productDescrip;
    public int count;
    public String licenseNumbers;
    public String price;
    public String isoOrigin;
    public String symbolOrigin;
    public String currencyRate;
    public int months;
    public String fullPrice;
    public String discountRate;
    public String totalPrice;
    
    public void copyFrom(LicenseFinance other){
        productId = other.productId;
        articul = other.articul;
        productDescrip = other.productDescrip;
        count = other.count;
        licenseNumbers = other.licenseNumbers;
        price = other.price;
        isoOrigin = other.isoOrigin;
        symbolOrigin = other.symbolOrigin;
        currencyRate = other.currencyRate;
        months = other.months;
        fullPrice = other.fullPrice;
        discountRate = other.discountRate;
        totalPrice = other.totalPrice;
    }
    
    @Override
    public String toString(){
        return  "productId: " + productId + "\n" + 
                "articul: " + articul + "\n" +
                "productDescrip: " + productDescrip + "\n" +
                "count: " + count + "\n" +
                "licenseNumbers: " + licenseNumbers + "\n" +
                "price: " + price + "\n" +
                "isoOrigin: " + isoOrigin + "\n" +
                "symbolOrigin: " + symbolOrigin + "\n" +
                "currencyRate: " + currencyRate + "\n" +
                "months: " + months + "\n" +
                "fullPrice: " + fullPrice + "\n" +
                "discountRate: " + discountRate + "\n" +
                "totalPrice: " + totalPrice + "\n";
    }
}
