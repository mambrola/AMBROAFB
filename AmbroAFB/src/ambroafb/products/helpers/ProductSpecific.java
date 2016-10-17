/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

/**
 *
 * @author dato
 */
public class ProductSpecific {

    private int recId;
    private int productSpecificId;
    public String descrip;

    public ProductSpecific() {
    }

    public int getRecId(){
        return recId;
    }
    
    public int getProductSpecificId(){
        return productSpecificId;
    }
    
    public String getDescrip(){
        return descrip;
    }
    
    
    public void setRecId(int recId){
        this.recId = recId;
    }

    public void setProductSpecificId(int productSpecificId){
        this.productSpecificId = productSpecificId;
    }
    
    public void setDescrip(String descrip){
        this.descrip = descrip;
    }
    
    
    public boolean equals(ProductSpecific prodSpecific){
        return this.getRecId() == prodSpecific.getRecId() && this.getDescrip().equals(prodSpecific.getDescrip());
    }
}
