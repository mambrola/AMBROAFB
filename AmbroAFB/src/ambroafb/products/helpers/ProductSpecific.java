/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dato
 */
public class ProductSpecific {

    private int recId;
    private final IntegerProperty productSpecificId;
    public final StringProperty descrip;
    @JsonIgnore
    public String language;

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
    
    public int getRecId(){
        return recId;
    }
    
    public int getProductSpecificId(){
        return productSpecificId.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }

    public void setProductSpecificId(int productSpecificId){
        this.productSpecificId.set(productSpecificId);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }
    
    public boolean equals(ProductSpecific prodSpecific){
        return  this.getProductSpecificId() == prodSpecific.getProductSpecificId() &&
                this.getDescrip().equals(prodSpecific.getDescrip());
    }
}
