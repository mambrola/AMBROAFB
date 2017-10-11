/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.NumberConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class DocMerchandise {
    
    private int recId, merchandise;
    private final StringProperty descrip, iso, vatRate;
    
    private final float vatPercentDefaultValue = 0;
    
    public DocMerchandise(){
        descrip = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        vatRate = new SimpleStringProperty("");
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty vatRateProperty(){
        return vatRate;
    }

    
    public int getRecId() {
        return recId;
    }

    public int getMerchandise() {
        return merchandise;
    }

    public String getDescrip() {
        return descrip.get();
    }

    public String getIso() {
        return iso.get();
    }

    public Float getVatRate() {
        return NumberConverter.stringToFloat(vatRate.get(), 2, vatPercentDefaultValue);
    }

    
    public void setRecId(int recId) {
        this.recId = recId;
    }

    public void setMerchandise(int merchandise) {
        this.merchandise = merchandise;
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setVatRate(float vatRate){
        this.vatRate.set("" + vatRate);
    }
    
    public int compares(DocMerchandise other){
        return getRecId() - other.getRecId();
    }

    @Override
    public String toString() {
        return descrip.get();
    }
    
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        DocMerchandise otherDocMerchandise = (DocMerchandise)other;
        return  getRecId() == otherDocMerchandise.getRecId() ||
                getDescrip().equals(otherDocMerchandise.getDescrip());
    }
}
