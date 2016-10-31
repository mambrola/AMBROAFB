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
public class InvoiceReissuings {
    
    private int recId;
    private final IntegerProperty invoiceReissuing_Id;
    private String language;
    private final StringProperty descrip;
    
    public InvoiceReissuings(){
        invoiceReissuing_Id = new SimpleIntegerProperty(0);
        descrip = new SimpleStringProperty("");
    }
    
    public IntegerProperty invoiceReissuingIdProperty(){
        return invoiceReissuing_Id;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    
    public int getRecId(){
        return recId;
    }
    
    public int getInvoiceReissuingId(){
        return invoiceReissuing_Id.get();
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
        this.invoiceReissuing_Id.set(reissuingId);
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
    
    public boolean equals(InvoiceReissuings other){
        return  this.getInvoiceReissuingId() == other.getInvoiceReissuingId() &&
                this.getDescrip().equals(other.getDescrip());
    }
}
