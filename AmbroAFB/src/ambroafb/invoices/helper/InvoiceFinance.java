/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.helper;

/**
 *
 * @author dato
 */
public class InvoiceFinance {
    
    public String sum, additionalDiscountRate, additionalDiscountSum, nettoSum, vatRate, vat, paySum, isoTotal, symbolTotal;
    
    public void copyFrom(InvoiceFinance other){
        sum = other.sum;
        additionalDiscountRate = other.additionalDiscountRate;
        additionalDiscountSum = other.additionalDiscountSum;
        nettoSum = other.nettoSum;
        vatRate = other.vatRate;
        vat = other.vat;
        paySum = other.paySum;
        isoTotal = other.isoTotal;
        symbolTotal = other.symbolTotal;
    }
    
    @Override
    public String toString(){
        return  "sum: " + sum + "\n" +
                "additionalDiscountRate: " + additionalDiscountRate + "\n" +
                "additionalDiscountSum: " + additionalDiscountSum + "\n" +
                "nettoSum: " + nettoSum + "\n" +
                "vatRate: " + vatRate + "\n" +
                "vat: " + vat + "\n" +
                "PaySum: " + paySum + "\n" +
                "isoTotal: " + isoTotal + "\n" +
                "symbolTotal: " + symbolTotal + "\n";
    }
    
    public boolean dataIsEmpty(){
        return  isNullOrEmpty(sum) && isNullOrEmpty(additionalDiscountSum) && 
                isNullOrEmpty(nettoSum) && isNullOrEmpty(vat) && isNullOrEmpty(paySum);
    }
    
    private boolean isNullOrEmpty(String variable){
        return variable == null || variable.isEmpty();
    }
}
