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
public class InvoiceStatusClarify {
    
    private int recId;
    private final IntegerProperty statusClarify;
    private String language;
    private final StringProperty statusClarifyDescrip;
    
    public InvoiceStatusClarify(){
        statusClarify = new SimpleIntegerProperty(0);
        statusClarifyDescrip = new SimpleStringProperty("");
    }
    
    public IntegerProperty clarifyIdProperty(){
        return statusClarify;
    }
    
    public StringProperty descripProperty(){
        return statusClarifyDescrip;
    }
    
    // Getters:
    public int getRecId(){
        return recId;
    }
    
    public String getLanguage(){
        return language;
    }
    
    public int getInvoiceStatusClarifyId(){
        return statusClarify.get();
    }
    
    public String getDescrip(){
        return statusClarifyDescrip.get();
    }
    
    
    // Setters:
    public void setRecId(int recId){
        this.recId = recId;
    }
    
    public void setLanguage(String language){
        this.language = language;
    }
    
    public void setInvoiceStatusClarifyId(int statusClarify){
        this.statusClarify.set(statusClarify);
    }
    
    public void setDescrip(String descrip){
        this.statusClarifyDescrip.set(descrip);
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    public void copyFrom(InvoiceStatusClarify other){
        setInvoiceStatusClarifyId(other.getInvoiceStatusClarifyId());
        setDescrip(other.getDescrip());
    }
    
    public boolean compares(InvoiceStatusClarify other){
        return  this.getInvoiceStatusClarifyId() == other.getInvoiceStatusClarifyId() &&
                this.getDescrip().equals(other.getDescrip());
    }
    
    /**
     * The method compares two InvoiceStatusClarify objects.
     * @param other Other object that is not null.
     * @return 
     * @see ambroafb.general.interfaces.EditorPanelable#compareById(ambroafb.general.interfaces.EditorPanelable)  EditorPanelable method "compareById"
     */
    public int compareById(InvoiceStatusClarify other){
        return getRecId() - other.getRecId();
    }
}
