/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
@SuppressWarnings("EqualsAndHashcode")
@JsonIgnoreProperties({"productSpecificDescrip"})
public class ProductSpecific {

    private int recId;
    private final IntegerProperty productSpecificId;
    private final StringProperty descrip;
    private String language;

    public ProductSpecific() {
        productSpecificId = new SimpleIntegerProperty(0);
        descrip = new SimpleStringProperty("");
    }

    public IntegerProperty specificProperty(){
        return productSpecificId;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    @JsonIgnore
    public int getRecId(){
        return recId;
    }
    
    public int getProductSpecificId(){
        return productSpecificId.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }

    @JsonIgnore
    public String getLanguage() {
        return language;
    }
    
    
    @JsonProperty
    public void setRecId(int recId){
        this.recId = recId;
    }

    public void setProductSpecificId(int productSpecificId){
        this.productSpecificId.set(productSpecificId);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }

    @JsonProperty
    public void setLanguage(String language) {
        this.language = language;
    }
    
    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    public void copyFrom(ProductSpecific other){
        if (other != null){
            setProductSpecificId(other.getProductSpecificId());
            setDescrip(other.getDescrip());
//            setRecId(other.getRecId());
        }
    }
    
    public boolean compares(ProductSpecific other){
        return  getProductSpecificId() == other.getProductSpecificId() &&
                getDescrip().equals(other.getDescrip());
    }
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object prodSpecific){
        if (prodSpecific == null) return false;
        ProductSpecific otherSpecific = (ProductSpecific) prodSpecific;
        return  getProductSpecificId() == otherSpecific.getProductSpecificId() &&
                getDescrip().equals(otherSpecific.getDescrip());
    }
}
