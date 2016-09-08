/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.helpers;

import ambroafb.general.GeneralConfig;

/**
 *
 * @author dato
 */
public class ProductSpecific {

    public String descrip_default;
    public String descrip_first;
    public String descrip_second;

    public ProductSpecific() {
    }

    public String getDescrip_default(){
        return descrip_default;
    }

    public String getDescrip_first(){
        return descrip_first;
    }
    
    public String getDescrip_second(){
        return descrip_second;
    }
    
    public void setDescrip_default(String descrip){
        this.descrip_default = descrip;
    }
    
    public void setDescrip_first(String descrip){
        this.descrip_first = descrip;
    }
    
    public void setDescrip_second(String descrip){
        this.descrip_second = descrip;
    }
    
    public String getValue() {
        String lang = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
        return (lang.equals("ka")) ? descrip_first
                : (lang.equals("en")) ? descrip_second
                : descrip_default;
    }
}
