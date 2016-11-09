/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.helper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
@SuppressWarnings("EqualsAndHashcode")
public class InvoiceReissuing {
    
    private int recId;
    private final IntegerProperty invoiceReissuingId;
    private String language;
    private final StringProperty descrip;
    
    public InvoiceReissuing(){
        invoiceReissuingId = new SimpleIntegerProperty(0);
        descrip = new SimpleStringProperty("");
    }
    
    public IntegerProperty invoiceReissuingIdProperty(){
        return invoiceReissuingId;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    
    public int getRecId(){
        return recId;
    }
    
    public int getInvoiceReissuingId(){
        return invoiceReissuingId.get();
    }
    
    public String getLanguage(){
        return language;
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public void setInvoiceReissuingId(int reissuingId){
        this.invoiceReissuingId.set(reissuingId);
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    public void copyFrom(InvoiceReissuing other) {
        if (other != null) {
            setInvoiceReissuingId(other.getInvoiceReissuingId());
            setDescrip(other.getDescrip());

            setRecId(other.getRecId());
            setLanguage(other.getLanguage());
        }
    }
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        InvoiceReissuing otherReissuing = (InvoiceReissuing) other;
        return  this.getInvoiceReissuingId() == otherReissuing.getInvoiceReissuingId() &&
                this.getDescrip().equals(otherReissuing.getDescrip());
    }
}
